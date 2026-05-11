package com.reverix.reverix.infrastructure.persistence.repository

import com.reverix.reverix.infrastructure.persistence.entity.TheatreEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TheatreJpaRepository : JpaRepository<TheatreEntity, Long> {
    fun findByCity(city: String): List<TheatreEntity>
}
