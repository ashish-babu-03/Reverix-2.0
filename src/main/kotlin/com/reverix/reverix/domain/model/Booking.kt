package com.reverix.reverix.domain.model

import java.time.LocalDateTime

data class Booking(
    val id: Long? = null,
    val user: User,
    val show: Show,
    val seats: List<Seat>,
    val status: BookingStatus,
    val idempotencyKey: String,
    val createdAt: LocalDateTime
)

enum class BookingStatus {
    PENDING, CONFIRMED, CANCELLED
}
