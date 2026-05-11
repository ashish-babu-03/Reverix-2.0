package com.reverix.reverix.domain.model

import java.time.LocalDateTime

data class Show(
    val id: Long? = null,
    val movie: Movie,
    val theatre: Theatre,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val totalSeats: Int,
    val availableSeats: Int
)
