package com.reverix.reverix.domain.model

import java.time.LocalDateTime

data class Review(
    val id: Long = 0,
    val userId: Long,
    val movieId: Long,
    val rating: Double,
    val comment: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
