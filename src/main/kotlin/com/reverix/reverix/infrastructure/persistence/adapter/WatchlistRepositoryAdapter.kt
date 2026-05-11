package com.reverix.reverix.infrastructure.persistence.adapter

import com.reverix.reverix.domain.model.WatchlistItem
import com.reverix.reverix.domain.port.output.WatchlistRepository
import com.reverix.reverix.infrastructure.persistence.mapper.toDomain
import com.reverix.reverix.infrastructure.persistence.mapper.toEntity
import com.reverix.reverix.infrastructure.persistence.repository.WatchlistJpaRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class WatchlistRepositoryAdapter(
    private val watchlistJpaRepository: WatchlistJpaRepository
) : WatchlistRepository {

    override fun save(item: WatchlistItem): WatchlistItem {
        return watchlistJpaRepository.save(item.toEntity()).toDomain()
    }

    @Transactional
    override fun deleteByUserIdAndMovieId(userId: Long, movieId: Long) {
        watchlistJpaRepository.deleteByUserIdAndMovieId(userId, movieId)
    }

    override fun findByUserId(userId: Long): List<WatchlistItem> {
        return watchlistJpaRepository.findByUserId(userId).map { it.toDomain() }
    }
}
