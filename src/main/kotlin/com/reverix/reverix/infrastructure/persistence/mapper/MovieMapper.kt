package com.reverix.reverix.infrastructure.persistence.mapper

import com.reverix.reverix.domain.model.Movie
import com.reverix.reverix.infrastructure.persistence.entity.MovieEntity
import org.springframework.stereotype.Component

@Component
class MovieMapper {
    fun MovieEntity.toDomain() = Movie(
        id = id,
        title = title,
        genre = genre,
        moodTags = moodTags.split(",").filter { it.isNotBlank() },
        language = language,
        rating = rating,
        duration = duration,
        posterUrl = posterUrl
    )

    fun Movie.toEntity() = MovieEntity(
        id = id ?: 0,
        title = title,
        genre = genre,
        moodTags = moodTags.joinToString(","),
        language = language,
        rating = rating,
        duration = duration,
        posterUrl = posterUrl
    )
}
