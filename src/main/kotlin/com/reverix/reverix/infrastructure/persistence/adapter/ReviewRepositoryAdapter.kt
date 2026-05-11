package com.reverix.reverix.infrastructure.persistence.adapter

import com.reverix.reverix.domain.model.Review
import com.reverix.reverix.domain.port.output.ReviewRepository
import com.reverix.reverix.infrastructure.persistence.mapper.toDomain
import com.reverix.reverix.infrastructure.persistence.mapper.toEntity
import com.reverix.reverix.infrastructure.persistence.repository.ReviewJpaRepository
import org.springframework.stereotype.Component

@Component
class ReviewRepositoryAdapter(
    private val reviewJpaRepository: ReviewJpaRepository
) : ReviewRepository {

    override fun save(review: Review): Review {
        return reviewJpaRepository.save(review.toEntity()).toDomain()
    }

    override fun findByMovieId(movieId: Long): List<Review> {
        return reviewJpaRepository.findByMovieId(movieId).map { it.toDomain() }
    }
}
