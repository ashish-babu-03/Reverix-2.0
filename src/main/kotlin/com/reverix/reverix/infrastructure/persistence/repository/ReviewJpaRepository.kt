package com.reverix.reverix.infrastructure.persistence.repository

import com.reverix.reverix.infrastructure.persistence.entity.ReviewEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReviewJpaRepository : JpaRepository<ReviewEntity, Long> {
    fun findByMovieId(movieId: Long): List<ReviewEntity>
}
