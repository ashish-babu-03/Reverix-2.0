package com.reverix.reverix.domain.port.output

import com.reverix.reverix.domain.model.Show

interface ShowRepository {
    fun findById(id: Long): Show?
    fun findByMovieId(movieId: Long): List<Show>
    fun findByTheatreIdAndMovieId(theatreId: Long, movieId: Long): List<Show>
    fun save(show: Show): Show
}
