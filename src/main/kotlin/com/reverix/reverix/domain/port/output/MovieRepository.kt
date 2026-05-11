package com.reverix.reverix.domain.port.output

import com.reverix.reverix.domain.model.Movie

interface MovieRepository {
    fun save(movie: Movie): Movie
    fun findById(id: Long): Movie?
    fun findAll(): List<Movie>
    fun findByMoodTag(tag: String): List<Movie>
}
