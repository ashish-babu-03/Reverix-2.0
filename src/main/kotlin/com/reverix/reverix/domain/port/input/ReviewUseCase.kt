package com.reverix.reverix.domain.port.input

import com.reverix.reverix.domain.model.Review

interface ReviewUseCase {
    fun submitReview(userId: Long, movieId: Long, rating: Double, comment: String): Review
    fun getReviewsByMovieId(movieId: Long): List<Review>
    fun getAverageRating(movieId: Long): Double
}
