package com.reverix.reverix.domain.port.input

import com.reverix.reverix.domain.model.Booking

interface BookSeatUseCase {
    fun lockSeats(userId: Long, showId: Long, seatIds: List<Long>, idempotencyKey: String): Booking
    fun confirmBooking(bookingId: Long, idempotencyKey: String): Booking
    fun cancelBooking(bookingId: Long, userId: Long): Booking
}
