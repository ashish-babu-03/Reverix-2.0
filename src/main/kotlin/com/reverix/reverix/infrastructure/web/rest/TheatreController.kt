package com.reverix.reverix.infrastructure.web.rest

import com.reverix.reverix.domain.port.output.ShowRepository
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/theatres")
class TheatreController(
    private val showRepository: ShowRepository
) {

    @GetMapping("/{id}/shows")
    fun getShowsForTheatreAndMovie(
        @PathVariable id: Long,
        @RequestParam movieId: Long
    ): List<TheatreShowResponse> {
        val shows = showRepository.findByTheatreIdAndMovieId(id, movieId)
        return shows.map {
            TheatreShowResponse(
                id = it.id ?: 0,
                startTime = it.startTime,
                endTime = it.endTime,
                availableSeats = it.availableSeats,
                totalSeats = it.totalSeats
            )
        }
    }
}

data class TheatreShowResponse(
    val id: Long,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val availableSeats: Int,
    val totalSeats: Int
)
