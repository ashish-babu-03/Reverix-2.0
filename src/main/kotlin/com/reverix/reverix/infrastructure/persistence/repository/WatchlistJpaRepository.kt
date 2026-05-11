package com.reverix.reverix.infrastructure.persistence.repository

import com.reverix.reverix.infrastructure.persistence.entity.WatchlistItemEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WatchlistJpaRepository : JpaRepository<WatchlistItemEntity, Long> {
    fun findByUserId(userId: Long): List<WatchlistItemEntity>
    fun deleteByUserIdAndMovieId(userId: Long, movieId: Long)
}
