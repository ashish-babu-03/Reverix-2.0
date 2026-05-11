package com.reverix.reverix.domain.model

data class Seat(
    val id: Long? = null,
    val show: Show,
    val seatNumber: String,
    val zone: Zone,
    val status: SeatStatus
)

enum class Zone {
    FRONT, MIDDLE, BACK
}

enum class SeatStatus {
    AVAILABLE, LOCKED, BOOKED
}
