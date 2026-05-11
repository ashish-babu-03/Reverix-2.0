package com.reverix.reverix.domain.port.output

import com.reverix.reverix.domain.model.Review

interface ReviewRepository {
    fun save(review: Review): Review
    fun findByMovieId(movieId: Long): List<Review>
}
