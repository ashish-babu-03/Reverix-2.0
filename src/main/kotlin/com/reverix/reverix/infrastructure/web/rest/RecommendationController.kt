package com.reverix.reverix.infrastructure.web.rest

import com.reverix.reverix.domain.model.Movie
import com.reverix.reverix.domain.port.input.RecommendMovieUseCase
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/recommend")
class RecommendationController(
    private val recommendMovieUseCase: RecommendMovieUseCase
) {

    @PostMapping
    fun recommend(@RequestBody request: RecommendRequest): List<Movie> {
        return recommendMovieUseCase.recommend(
            mood = request.mood,
            city = request.city,
            groupType = request.groupType,
            groupSize = request.groupSize
        )
    }
}

data class RecommendRequest(
    val mood: String,
    val city: String,
    val groupType: String,
    val groupSize: Int
)
