package com.reverix.reverix.domain.port.input

import com.reverix.reverix.domain.model.Movie

interface GetMovieUseCase {
    fun getMovieById(id: Long): Movie
    fun getAllMovies(): List<Movie>
    fun getMoviesByMood(mood: String): List<Movie>
}
