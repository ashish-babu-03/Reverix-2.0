package com.reverix.reverix.domain.port.output

import com.reverix.reverix.domain.model.Booking

interface BookingRepository {
    fun save(booking: Booking): Booking
    fun findById(id: Long): Booking?
    fun findByIdempotencyKey(key: String): Booking?
    fun findByUserId(userId: Long): List<Booking>
}
