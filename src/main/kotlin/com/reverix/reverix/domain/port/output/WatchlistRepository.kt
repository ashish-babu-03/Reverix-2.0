package com.reverix.reverix.domain.port.output

import com.reverix.reverix.domain.model.WatchlistItem

interface WatchlistRepository {
    fun save(item: WatchlistItem): WatchlistItem
    fun deleteByUserIdAndMovieId(userId: Long, movieId: Long)
    fun findByUserId(userId: Long): List<WatchlistItem>
}
