package com.reverix.reverix.domain.port.output

import com.reverix.reverix.domain.model.Seat

interface SeatRepository {
    fun findByShowId(showId: Long): List<Seat>
    fun findByIds(ids: List<Long>): List<Seat>
    fun saveAll(seats: List<Seat>): List<Seat>
}
