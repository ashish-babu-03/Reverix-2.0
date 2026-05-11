package com.reverix.reverix.infrastructure.persistence.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "bookings")
data class BookingEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val userId: Long,
    val showId: Long,
    val seatIds: String,
    val status: String,
    @Column(unique = true)
    val idempotencyKey: String,
    val createdAt: LocalDateTime,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    val user: UserEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showId", insertable = false, updatable = false)
    val show: ShowEntity? = null,

    @OneToMany
    @JoinTable(
        name = "booking_seats",
        joinColumns = [JoinColumn(name = "booking_id")],
        inverseJoinColumns = [JoinColumn(name = "seat_id")]
    )
    val seats: List<SeatEntity> = emptyList()
)
