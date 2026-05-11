package com.reverix.reverix.infrastructure.persistence.mapper

import com.reverix.reverix.domain.model.WatchlistItem
import com.reverix.reverix.infrastructure.persistence.entity.WatchlistItemEntity

fun WatchlistItemEntity.toDomain(): WatchlistItem {
    return WatchlistItem(
        id = id,
        userId = userId,
        movieId = movieId,
        addedAt = addedAt
    )
}

fun WatchlistItem.toEntity(): WatchlistItemEntity {
    return WatchlistItemEntity(
        id = id,
        userId = userId,
        movieId = movieId,
        addedAt = addedAt
    )
}
