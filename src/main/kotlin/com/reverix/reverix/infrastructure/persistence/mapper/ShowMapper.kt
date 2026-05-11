package com.reverix.reverix.infrastructure.persistence.mapper

import com.reverix.reverix.domain.model.*
import com.reverix.reverix.infrastructure.persistence.entity.ShowEntity
import org.springframework.stereotype.Component

@Component
class ShowMapper {
    fun ShowEntity.toDomain(): Show {
        val dummyMovie = Movie(id = movieId, title = "", genre = "", moodTags = emptyList(), language = "", rating = null, duration = 0, posterUrl = null)
        val dummyTheatre = Theatre(id = theatreId, name = "", city = "", vibe = Vibe.SILENT)
        
        return Show(
            id = id,
            movie = dummyMovie,
            theatre = dummyTheatre,
            startTime = startTime,
            endTime = endTime,
            totalSeats = totalSeats,
            availableSeats = availableSeats
        )
    }

    fun Show.toEntity() = ShowEntity(
        id = id ?: 0,
        movieId = movie.id ?: 0,
        theatreId = theatre.id ?: 0,
        startTime = startTime,
        endTime = endTime,
        totalSeats = totalSeats,
        availableSeats = availableSeats
    )
}
