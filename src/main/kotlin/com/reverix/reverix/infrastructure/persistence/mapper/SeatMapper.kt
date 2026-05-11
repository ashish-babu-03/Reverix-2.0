package com.reverix.reverix.infrastructure.persistence.mapper

import com.reverix.reverix.domain.model.*
import com.reverix.reverix.infrastructure.persistence.entity.SeatEntity
import com.reverix.reverix.infrastructure.persistence.entity.ShowEntity
import com.reverix.reverix.infrastructure.persistence.repository.ShowJpaRepository
import org.springframework.stereotype.Component

@Component
class SeatMapper(
    private val showJpaRepository: ShowJpaRepository,
    private val showMapper: ShowMapper
) {
    fun SeatEntity.toDomain(): Seat {
        with(showMapper) {
            return Seat(
                id = id,
                show = show.toDomain(),
                seatNumber = seatNumber,
                zone = Zone.valueOf(zone),
                status = SeatStatus.valueOf(status)
            )
        }
    }

    fun Seat.toEntity(): SeatEntity {
        val showEntity = showJpaRepository.findById(show.id ?: 0)
            .orElseThrow { IllegalStateException("Show not found: ${show.id}") }
        return SeatEntity(
            id = id ?: 0,
            show = showEntity,
            seatNumber = seatNumber,
            zone = zone.name,
            status = status.name
        )
    }
}