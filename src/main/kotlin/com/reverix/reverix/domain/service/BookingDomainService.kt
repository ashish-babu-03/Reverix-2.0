package com.reverix.reverix.domain.service

import com.reverix.reverix.domain.model.*
import com.reverix.reverix.domain.port.input.BookSeatUseCase
import com.reverix.reverix.domain.port.output.BookingRepository
import com.reverix.reverix.domain.port.output.CachePort
import com.reverix.reverix.domain.port.output.SeatRepository
import java.time.LocalDateTime

class BookingDomainService(
    private val bookingRepository: BookingRepository,
    private val seatRepository: SeatRepository,
    private val showRepository: com.reverix.reverix.domain.port.output.ShowRepository,
    private val cachePort: CachePort
) : BookSeatUseCase {

    override fun lockSeats(userId: Long, showId: Long, seatIds: List<Long>, idempotencyKey: String): Booking {
        val existingBooking = bookingRepository.findByIdempotencyKey(idempotencyKey)
        if (existingBooking != null) {
            return existingBooking
        }

        val seats = seatRepository.findByIds(seatIds)
        if (seats.isEmpty()) {
            throw IllegalArgumentException("No seats found")
        }

        for (seat in seats) {
            val seatId = seat.id ?: throw IllegalArgumentException("Seat ID cannot be null")
            if (cachePort.isSeatLocked(seatId)) {
                throw SeatAlreadyLockedException(seatId)
            }
        }

        for (seat in seats) {
            val seatId = seat.id!!
            cachePort.lockSeat(seatId, userId, 600L)
        }

        val updatedSeats = seats.map { it.copy(status = SeatStatus.BOOKED) }
        seatRepository.saveAll(updatedSeats)

        val show = showRepository.findById(showId) ?: throw IllegalArgumentException("Show not found")
        val updatedShow = show.copy(availableSeats = show.availableSeats - updatedSeats.size)
        showRepository.save(updatedShow)
        
        val user = User(id = userId, email = "", passwordHash = "", role = Role.USER, createdAt = LocalDateTime.now())

        val booking = Booking(
            user = user,
            show = updatedShow,
            seats = updatedSeats,
            status = BookingStatus.PENDING,
            idempotencyKey = idempotencyKey,
            createdAt = LocalDateTime.now()
        )

        return bookingRepository.save(booking)
    }

    override fun confirmBooking(bookingId: Long, idempotencyKey: String): Booking {
        val booking = bookingRepository.findById(bookingId) ?: throw BookingNotFoundException(bookingId)
        
        if (booking.status == BookingStatus.CONFIRMED) {
            return booking
        }

        if (booking.status != BookingStatus.PENDING) {
            throw IllegalArgumentException("Booking is not PENDING")
        }

        val confirmedBooking = booking.copy(status = BookingStatus.CONFIRMED)

        confirmedBooking.seats.forEach { seat ->
            seat.id?.let { cachePort.unlockSeat(it) }
        }

        return bookingRepository.save(confirmedBooking)
    }

    override fun cancelBooking(bookingId: Long, userId: Long): Booking {
        val booking = bookingRepository.findById(bookingId) ?: throw BookingNotFoundException(bookingId)

        if (booking.user.id != userId) {
            throw UnauthorizedBookingException(userId)
        }

        val cancelledBooking = booking.copy(status = BookingStatus.CANCELLED)

        booking.seats.forEach { seat ->
            seat.id?.let { cachePort.unlockSeat(it) }
        }

        return bookingRepository.save(cancelledBooking)
    }
}
