package com.reverix.reverix.infrastructure.persistence

import com.reverix.reverix.domain.model.Movie
import com.reverix.reverix.domain.port.output.MovieRepository
import com.reverix.reverix.infrastructure.persistence.mapper.MovieMapper
import com.reverix.reverix.infrastructure.persistence.repository.MovieJpaRepository
import org.springframework.stereotype.Component

@Component
class MovieRepositoryAdapter(
    private val movieJpaRepository: MovieJpaRepository,
    private val movieMapper: MovieMapper
) : MovieRepository {

    override fun save(movie: Movie): Movie {
        with(movieMapper) {
            val entity = movie.toEntity()
            return movieJpaRepository.save(entity).toDomain()
        }
    }

    override fun findById(id: Long): Movie? {
        with(movieMapper) {
            return movieJpaRepository.findById(id).orElse(null)?.toDomain()
        }
    }

    override fun findAll(): List<Movie> {
        with(movieMapper) {
            return movieJpaRepository.findAll().map { it.toDomain() }
        }
    }

    override fun findByMoodTag(tag: String): List<Movie> {
        with(movieMapper) {
            return movieJpaRepository.findByMoodTagsContaining(tag).map { it.toDomain() }
        }
    }
}
