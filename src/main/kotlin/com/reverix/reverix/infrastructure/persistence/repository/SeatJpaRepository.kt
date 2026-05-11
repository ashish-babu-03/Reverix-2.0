package com.reverix.reverix.infrastructure.persistence.repository

import com.reverix.reverix.infrastructure.persistence.entity.SeatEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SeatJpaRepository : JpaRepository<SeatEntity, Long> {
    fun findByShowId(showId: Long): List<SeatEntity>
}
