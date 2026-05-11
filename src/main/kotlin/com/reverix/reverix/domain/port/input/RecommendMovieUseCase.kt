package com.reverix.reverix.domain.port.input

import com.reverix.reverix.domain.model.Movie

interface RecommendMovieUseCase {
    fun recommend(mood: String, city: String, groupType: String, groupSize: Int): List<Movie>
}
