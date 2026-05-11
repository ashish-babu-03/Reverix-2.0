package com.reverix.reverix.infrastructure.persistence.repository

import com.reverix.reverix.infrastructure.persistence.entity.ShowEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ShowJpaRepository : JpaRepository<ShowEntity, Long> {
    fun findByMovieId(movieId: Long): List<ShowEntity>
    fun findByTheatreIdAndMovieId(theatreId: Long, movieId: Long): List<ShowEntity>
}
