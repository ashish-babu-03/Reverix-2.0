package com.reverix.reverix.infrastructure.web.rest

import com.reverix.reverix.application.usecase.BookingOrchestrator
import com.reverix.reverix.domain.model.Booking
import com.reverix.reverix.domain.model.UnauthorizedBookingEmailException
import com.reverix.reverix.domain.port.input.BookSeatUseCase
import com.reverix.reverix.domain.port.output.BookingRepository
import com.reverix.reverix.infrastructure.persistence.repository.UserJpaRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/bookings")
class BookingController(
    private val bookingOrchestrator: BookingOrchestrator,
    private val bookSeatUseCase: BookSeatUseCase,
    private val bookingRepository: BookingRepository,
    private val userJpaRepository: UserJpaRepository
) {

    private fun getCurrentUserId(): Long {
        val authentication = SecurityContextHolder.getContext().authentication
        val email = authentication?.name ?: throw UnauthorizedBookingEmailException("Not authenticated")
        val user = userJpaRepository.findByEmail(email) 
            ?: throw IllegalArgumentException("User not found")
        return user.id
    }

    @PostMapping("/lock")
    fun lockSeats(@RequestBody request: LockSeatsRequest): Booking {
        return bookingOrchestrator.lockAndNotify(
            userId = getCurrentUserId(),
            showId = request.showId,
            seatIds = request.seatIds,
            idempotencyKey = request.idempotencyKey
        )
    }

    @PostMapping("/confirm")
    fun confirmBooking(@RequestBody request: ConfirmBookingRequest): Booking {
        getCurrentUserId() // Ensure authenticated user exists
        return bookingOrchestrator.confirmAndNotify(
            bookingId = request.bookingId,
            idempotencyKey = request.idempotencyKey
        )
    }

    @DeleteMapping("/{id}")
    fun cancelBooking(@PathVariable id: Long): Booking {
        return bookSeatUseCase.cancelBooking(
            bookingId = id,
            userId = getCurrentUserId()
        )
    }

    @GetMapping("/my-bookings")
    fun getMyBookings(): List<Booking> {
        return bookingRepository.findByUserId(getCurrentUserId())
    }
}

data class LockSeatsRequest(val showId: Long, val seatIds: List<Long>, val idempotencyKey: String)
data class ConfirmBookingRequest(val bookingId: Long, val idempotencyKey: String)
