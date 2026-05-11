package com.reverix.reverix.domain.model

import java.time.LocalDateTime

data class WatchlistItem(
    val id: Long = 0,
    val userId: Long,
    val movieId: Long,
    val addedAt: LocalDateTime = LocalDateTime.now()
)
