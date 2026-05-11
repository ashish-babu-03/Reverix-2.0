package com.reverix.reverix.domain.service

import com.reverix.reverix.domain.model.WatchlistItem
import com.reverix.reverix.domain.port.input.WatchlistUseCase
import com.reverix.reverix.domain.port.output.WatchlistRepository

class WatchlistDomainService(
    private val watchlistRepository: WatchlistRepository
) : WatchlistUseCase {

    override fun addToWatchlist(userId: Long, movieId: Long): WatchlistItem {
        val existing = watchlistRepository.findByUserId(userId).find { it.movieId == movieId }
        if (existing != null) {
            return existing
        }
        val item = WatchlistItem(
            userId = userId,
            movieId = movieId
        )
        return watchlistRepository.save(item)
    }

    override fun removeFromWatchlist(userId: Long, movieId: Long) {
        watchlistRepository.deleteByUserIdAndMovieId(userId, movieId)
    }

    override fun getWatchlist(userId: Long): List<WatchlistItem> {
        return watchlistRepository.findByUserId(userId)
    }
}
