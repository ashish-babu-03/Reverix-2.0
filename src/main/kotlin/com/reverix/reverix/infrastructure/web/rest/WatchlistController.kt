package com.reverix.reverix.infrastructure.web.rest

import com.reverix.reverix.domain.model.WatchlistItem
import com.reverix.reverix.domain.port.input.WatchlistUseCase
import com.reverix.reverix.infrastructure.persistence.repository.UserJpaRepository
import com.reverix.reverix.infrastructure.security.JwtUtil
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/watchlist")
class WatchlistController(
    private val watchlistUseCase: WatchlistUseCase,
    private val userJpaRepository: UserJpaRepository,
    private val jwtUtil: JwtUtil
) {

    private fun getUserIdFromToken(authHeader: String): Long {
        val token = authHeader.substring(7)
        val email = jwtUtil.extractEmail(token)
        val user = userJpaRepository.findByEmail(email) 
            ?: throw IllegalArgumentException("User not found")
        return user.id
    }

    @PostMapping("/{movieId}")
    fun addToWatchlist(
        @RequestHeader("Authorization") authHeader: String,
        @PathVariable movieId: Long
    ): WatchlistItem {
        val userId = getUserIdFromToken(authHeader)
        return watchlistUseCase.addToWatchlist(userId, movieId)
    }

    @DeleteMapping("/{movieId}")
    fun removeFromWatchlist(
        @RequestHeader("Authorization") authHeader: String,
        @PathVariable movieId: Long
    ) {
        val userId = getUserIdFromToken(authHeader)
        watchlistUseCase.removeFromWatchlist(userId, movieId)
    }

    @GetMapping
    fun getWatchlist(
        @RequestHeader("Authorization") authHeader: String
    ): List<WatchlistItem> {
        val userId = getUserIdFromToken(authHeader)
        return watchlistUseCase.getWatchlist(userId)
    }
}
