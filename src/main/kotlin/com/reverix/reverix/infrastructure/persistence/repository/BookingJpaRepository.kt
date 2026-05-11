package com.reverix.reverix.infrastructure.persistence.repository

import com.reverix.reverix.infrastructure.persistence.entity.BookingEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface BookingJpaRepository : JpaRepository<BookingEntity, Long> {
    fun findByIdempotencyKey(key: String): BookingEntity?
    fun findByUserId(userId: Long): List<BookingEntity>

    @Query("SELECT b FROM BookingEntity b LEFT JOIN FETCH b.show s LEFT JOIN FETCH s.movie LEFT JOIN FETCH s.theatre LEFT JOIN FETCH b.seats LEFT JOIN FETCH b.user WHERE b.user.id = :userId")
    fun findByUserIdWithDetails(userId: Long): List<BookingEntity>
}
