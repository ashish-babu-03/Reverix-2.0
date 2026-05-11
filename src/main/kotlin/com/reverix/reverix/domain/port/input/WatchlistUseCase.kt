package com.reverix.reverix.domain.port.input

import com.reverix.reverix.domain.model.WatchlistItem

interface WatchlistUseCase {
    fun addToWatchlist(userId: Long, movieId: Long): WatchlistItem
    fun removeFromWatchlist(userId: Long, movieId: Long)
    fun getWatchlist(userId: Long): List<WatchlistItem>
}
