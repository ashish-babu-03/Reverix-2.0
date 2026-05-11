package com.reverix.reverix.infrastructure.persistence.repository

import com.reverix.reverix.infrastructure.persistence.entity.BookingEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface BookingJpaRepository : JpaRepository<BookingEntity, Long> {

    @Query("SELECT b FROM BookingEntity b LEFT JOIN FETCH b.user LEFT JOIN FETCH b.show s LEFT JOIN FETCH s.movie LEFT JOIN FETCH s.theatre WHERE b.idempotencyKey = :key")
    fun findByIdempotencyKey(@Param("key") key: String): BookingEntity?

    @Query("SELECT b FROM BookingEntity b LEFT JOIN FETCH b.user LEFT JOIN FETCH b.show s LEFT JOIN FETCH s.movie LEFT JOIN FETCH s.theatre WHERE b.user.id = :userId")
    fun findByUserId(@Param("userId") userId: Long): List<BookingEntity>

    @Query("SELECT b FROM BookingEntity b LEFT JOIN FETCH b.user LEFT JOIN FETCH b.show s LEFT JOIN FETCH s.movie LEFT JOIN FETCH s.theatre WHERE b.id = :id")
    fun findByIdWithDetails(@Param("id") id: Long): BookingEntity?

    @Query("SELECT b FROM BookingEntity b LEFT JOIN FETCH b.user LEFT JOIN FETCH b.show s LEFT JOIN FETCH s.movie LEFT JOIN FETCH s.theatre WHERE b.idempotencyKey = :key")
    fun findByIdempotencyKeyWithDetails(@Param("key") key: String): BookingEntity?

    @Query("SELECT b FROM BookingEntity b LEFT JOIN FETCH b.user LEFT JOIN FETCH b.show s LEFT JOIN FETCH s.movie LEFT JOIN FETCH s.theatre WHERE b.user.id = :userId")
    fun findByUserIdWithDetails(@Param("userId") userId: Long): List<BookingEntity>
}