package com.reverix.reverix.infrastructure.persistence

import com.reverix.reverix.domain.model.Seat
import com.reverix.reverix.domain.port.output.SeatRepository
import com.reverix.reverix.infrastructure.persistence.mapper.SeatMapper
import com.reverix.reverix.infrastructure.persistence.repository.SeatJpaRepository
import org.springframework.stereotype.Component

@Component
class SeatRepositoryAdapter(
    private val seatJpaRepository: SeatJpaRepository,
    private val seatMapper: SeatMapper
) : SeatRepository {

    override fun findByShowId(showId: Long): List<Seat> {
        with(seatMapper) {
            return seatJpaRepository.findByShowId(showId).map { it.toDomain() }
        }
    }

    override fun findByIds(ids: List<Long>): List<Seat> {
        with(seatMapper) {
            return seatJpaRepository.findAllById(ids).map { it.toDomain() }
        }
    }

    override fun saveAll(seats: List<Seat>): List<Seat> {
        with(seatMapper) {
            val entities = seats.map { it.toEntity() }
            return seatJpaRepository.saveAll(entities).map { it.toDomain() }
        }
    }
}
