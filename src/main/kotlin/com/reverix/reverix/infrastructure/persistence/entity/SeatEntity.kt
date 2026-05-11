package com.reverix.reverix.infrastructure.persistence.entity

import jakarta.persistence.*

@Entity
@Table(name = "seats")
data class SeatEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val seatNumber: String,
    val zone: String,
    val status: String,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "show_id")
    val show: ShowEntity
)