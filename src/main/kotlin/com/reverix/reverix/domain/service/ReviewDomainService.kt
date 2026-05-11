package com.reverix.reverix.domain.service

import com.reverix.reverix.domain.model.Review
import com.reverix.reverix.domain.port.input.ReviewUseCase
import com.reverix.reverix.domain.port.output.ReviewRepository

class ReviewDomainService(
    private val reviewRepository: ReviewRepository
) : ReviewUseCase {

    override fun submitReview(userId: Long, movieId: Long, rating: Double, comment: String): Review {
        require(rating in 0.0..5.0) { "Rating must be between 0.0 and 5.0" }
        require(comment.isNotBlank()) { "Comment cannot be blank" }
        
        val review = Review(
            userId = userId,
            movieId = movieId,
            rating = rating,
            comment = comment
        )
        return reviewRepository.save(review)
    }

    override fun getReviewsByMovieId(movieId: Long): List<Review> {
        return reviewRepository.findByMovieId(movieId)
    }

    override fun getAverageRating(movieId: Long): Double {
        val reviews = reviewRepository.findByMovieId(movieId)
        if (reviews.isEmpty()) return 0.0
        return reviews.map { it.rating }.average()
    }
}
