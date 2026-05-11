package com.reverix.reverix.domain.service

import com.reverix.reverix.domain.model.Movie
import com.reverix.reverix.domain.port.input.GetMovieUseCase
import com.reverix.reverix.domain.port.output.MovieRepository

class MovieDomainService(
    private val movieRepository: MovieRepository
) : GetMovieUseCase {

    override fun getMovieById(id: Long): Movie {
        return movieRepository.findById(id) ?: throw IllegalArgumentException("Movie with ID $id not found")
    }

    override fun getAllMovies(): List<Movie> {
        return movieRepository.findAll()
    }

    override fun getMoviesByMood(mood: String): List<Movie> {
        return movieRepository.findAll().filter { it.moodTags.contains(mood) }
    }
}
