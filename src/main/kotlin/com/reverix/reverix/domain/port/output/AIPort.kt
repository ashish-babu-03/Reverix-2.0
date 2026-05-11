package com.reverix.reverix.domain.port.output

import com.reverix.reverix.domain.model.Movie

interface AIPort {
    fun getRecommendations(mood: String, movies: List<Movie>): List<Movie>
    fun chat(userMessage: String, context: String): String
}
