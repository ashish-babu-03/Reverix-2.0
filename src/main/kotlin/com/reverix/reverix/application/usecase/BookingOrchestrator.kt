package com.reverix.reverix.application.usecase

import com.reverix.reverix.domain.model.Booking
import com.reverix.reverix.domain.port.input.BookSeatUseCase
import com.reverix.reverix.domain.port.output.SeatRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class BookingOrchestrator(
    private val bookSeatUseCase: BookSeatUseCase,
    private val seatRepository: SeatRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    fun lockAndNotify(userId: Long, showId: Long, seatIds: List<Long>, idempotencyKey: String): Booking {
        val booking = bookSeatUseCase.lockSeats(userId, showId, seatIds, idempotencyKey)
        
        booking.id?.let { bookingId ->
            eventPublisher.publishEvent(SeatLockedEvent(bookingId, userId, seatIds))
        }
        
        return booking
    }

    fun confirmAndNotify(bookingId: Long, idempotencyKey: String): Booking {
        val booking = bookSeatUseCase.confirmBooking(bookingId, idempotencyKey)
        
        booking.id?.let {
            val userId = booking.user.id ?: 0L
            eventPublisher.publishEvent(BookingConfirmedEvent(it, userId))
        }
        
        return booking
    }
}
