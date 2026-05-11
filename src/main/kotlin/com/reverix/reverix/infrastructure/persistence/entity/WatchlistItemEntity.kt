package com.reverix.reverix.infrastructure.persistence.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "watchlist_items")
data class WatchlistItemEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val userId: Long,
    val movieId: Long,
    val addedAt: LocalDateTime
)
