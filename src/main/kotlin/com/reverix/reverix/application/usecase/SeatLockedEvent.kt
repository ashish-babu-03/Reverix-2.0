package com.reverix.reverix.application.usecase

data class SeatLockedEvent(val bookingId: Long, val userId: Long, val seatIds: List<Long>)
