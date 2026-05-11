package com.reverix.reverix.infrastructure.persistence.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "bookings")
data class BookingEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val seatIds: String = "",
    val status: String,
    @Column(unique = true)
    val idempotencyKey: String,
    val createdAt: LocalDateTime,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    val user: UserEntity,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "show_id")
    val show: ShowEntity,

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "booking_seats",
        joinColumns = [JoinColumn(name = "booking_id")],
        inverseJoinColumns = [JoinColumn(name = "seat_id")]
    )
    val seats: List<SeatEntity> = emptyList()
)