package com.reverix.reverix.infrastructure.persistence.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "shows")
data class ShowEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val movieId: Long,
    val theatreId: Long,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val totalSeats: Int,
    val availableSeats: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movieId", insertable = false, updatable = false)
    val movie: MovieEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theatreId", insertable = false, updatable = false)
    val theatre: TheatreEntity? = null
)
