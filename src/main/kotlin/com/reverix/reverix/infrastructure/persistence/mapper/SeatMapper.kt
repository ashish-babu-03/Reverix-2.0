package com.reverix.reverix.infrastructure.persistence.mapper

import com.reverix.reverix.domain.model.*
import com.reverix.reverix.infrastructure.persistence.entity.SeatEntity
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SeatMapper {
    fun SeatEntity.toDomain(): Seat {
        val dummyMovie = Movie(title = "", genre = "", moodTags = emptyList(), language = "", rating = null, duration = 0, posterUrl = null)
        val dummyTheatre = Theatre(name = "", city = "", vibe = Vibe.SILENT)
        val dummyShow = Show(id = showId, movie = dummyMovie, theatre = dummyTheatre, startTime = LocalDateTime.now(), endTime = LocalDateTime.now(), totalSeats = 0, availableSeats = 0)
        
        return Seat(
            id = id,
            show = dummyShow,
            seatNumber = seatNumber,
            zone = Zone.valueOf(zone),
            status = SeatStatus.valueOf(status)
        )
    }

    fun Seat.toEntity() = SeatEntity(
        id = id ?: 0,
        showId = show.id ?: 0,
        seatNumber = seatNumber,
        zone = zone.name,
        status = status.name
    )
}
