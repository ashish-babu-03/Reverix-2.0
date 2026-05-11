package com.reverix.reverix.infrastructure.persistence.mapper

import com.reverix.reverix.domain.model.*
import com.reverix.reverix.infrastructure.persistence.entity.MovieEntity
import com.reverix.reverix.infrastructure.persistence.entity.ShowEntity
import com.reverix.reverix.infrastructure.persistence.entity.TheatreEntity
import org.springframework.stereotype.Component

@Component
class ShowMapper {

    fun ShowEntity.toDomain(): Show {
        val movie = Movie(
            id = movie.id,
            title = movie.title,
            genre = movie.genre,
            moodTags = movie.moodTags.split(",").map { it.trim() },
            language = movie.language,
            rating = movie.rating,
            duration = movie.duration,
            posterUrl = movie.posterUrl
        )

        val theatre = Theatre(
            id = theatre.id,
            name = theatre.name,
            city = theatre.city,
            vibe = Vibe.valueOf(theatre.vibe)
        )

        return Show(
            id = id,
            movie = movie,
            theatre = theatre,
            startTime = startTime,
            endTime = endTime,
            totalSeats = totalSeats,
            availableSeats = availableSeats
        )
    }

    fun Show.toEntity() = ShowEntity(
        id = id ?: 0,
        movie = MovieEntity(
            id = movie.id ?: 0,
            title = movie.title,
            genre = movie.genre,
            moodTags = movie.moodTags.joinToString(","),
            language = movie.language,
            rating = movie.rating ?: 0.0,
            duration = movie.duration,
            posterUrl = movie.posterUrl
        ),
        theatre = TheatreEntity(
            id = theatre.id ?: 0,
            name = theatre.name,
            city = theatre.city,
            vibe = theatre.vibe.name
        ),
        startTime = startTime,
        endTime = endTime,
        totalSeats = totalSeats,
        availableSeats = availableSeats
    )
}