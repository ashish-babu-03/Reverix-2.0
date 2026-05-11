package com.reverix.reverix.infrastructure.web.rest

import com.reverix.reverix.domain.model.Review
import com.reverix.reverix.domain.model.TransactionType
import com.reverix.reverix.domain.port.input.CoinUseCase
import com.reverix.reverix.domain.port.input.ReviewUseCase
import com.reverix.reverix.infrastructure.persistence.repository.UserJpaRepository
import com.reverix.reverix.infrastructure.security.JwtUtil
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/reviews")
class ReviewController(
    private val reviewUseCase: ReviewUseCase,
    private val coinUseCase: CoinUseCase,
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

    @PostMapping
    fun submitReview(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody request: SubmitReviewRequest
    ): Review {
        val userId = getUserIdFromToken(authHeader)
        val review = reviewUseCase.submitReview(
            userId = userId,
            movieId = request.movieId,
            rating = request.rating,
            comment = request.comment
        )
        coinUseCase.earnCoins(userId, TransactionType.REVIEW)
        return review
    }

    @GetMapping("/{movieId}")
    fun getReviewsByMovieId(@PathVariable movieId: Long): List<Review> {
        return reviewUseCase.getReviewsByMovieId(movieId)
    }

    @GetMapping("/{movieId}/average")
    fun getAverageRating(@PathVariable movieId: Long): Map<String, Double> {
        return mapOf("averageRating" to reviewUseCase.getAverageRating(movieId))
    }
}

data class SubmitReviewRequest(
    val movieId: Long,
    val rating: Double,
    val comment: String
)
