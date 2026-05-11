package com.reverix.reverix.domain.port.input

import com.reverix.reverix.domain.model.Seat
import com.reverix.reverix.domain.model.Show

interface ManageShowUseCase {
    fun getShowsByMovie(movieId: Long): List<Show>
    fun getAvailableSeats(showId: Long): List<Seat>
}
