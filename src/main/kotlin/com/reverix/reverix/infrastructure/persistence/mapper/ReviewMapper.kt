package com.reverix.reverix.infrastructure.persistence.mapper

import com.reverix.reverix.domain.model.Review
import com.reverix.reverix.infrastructure.persistence.entity.ReviewEntity

fun ReviewEntity.toDomain(): Review {
    return Review(
        id = id,
        userId = userId,
        movieId = movieId,
        rating = rating,
        comment = comment,
        createdAt = createdAt
    )
}

fun Review.toEntity(): ReviewEntity {
    return ReviewEntity(
        id = id,
        userId = userId,
        movieId = movieId,
        rating = rating,
        comment = comment,
        createdAt = createdAt
    )
}
