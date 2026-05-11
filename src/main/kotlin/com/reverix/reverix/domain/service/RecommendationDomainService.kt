package com.reverix.reverix.domain.service

import com.reverix.reverix.domain.model.Movie
import com.reverix.reverix.domain.port.input.RecommendMovieUseCase
import com.reverix.reverix.domain.port.output.AIPort
import com.reverix.reverix.domain.port.output.MovieRepository

class RecommendationDomainService(
    private val movieRepository: MovieRepository,
    private val aiPort: AIPort
) : RecommendMovieUseCase {

    override fun recommend(mood: String, city: String, groupType: String, groupSize: Int): List<Movie> {
        val movies = movieRepository.findAll()
        return aiPort.getRecommendations(mood, movies)
    }
}
