package com.reverix.reverix.infrastructure.persistence.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "shows")
data class ShowEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val totalSeats: Int,
    val availableSeats: Int,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "movie_id")
    val movie: MovieEntity,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "theatre_id")
    val theatre: TheatreEntity
)