package com.reverix.reverix.infrastructure.web.rest

import com.reverix.reverix.domain.port.input.ManageShowUseCase
import org.springframework.web.bind.annotation.*
import kotlin.toString

@RestController
@RequestMapping("/api/shows")
class ShowController(
    private val manageShowUseCase: ManageShowUseCase
) {

    @GetMapping("/{id}/seats")
    fun getAvailableSeats(@PathVariable id: Long): List<SeatResponse> {
        val seats = manageShowUseCase.getAvailableSeats(id)
        return seats.map {
            SeatResponse(
                id = it.id ?: 0,
                seatNumber = it.seatNumber,
                zone = it.zone.name,
                status = it.status.name
            )
        }
    }

    @GetMapping("/movie/{movieId}")
    fun getShowsByMovie(@PathVariable movieId: Long): List<ShowResponse> {
        val shows = manageShowUseCase.getShowsByMovie(movieId)
        return shows.map {
            ShowResponse(
                id = it.id ?: 0,
                movieId = it.movie.id ?: 0,
                movieTitle = it.movie.title,
                theatreId = it.theatre.id ?: 0,
                theatreName = it.theatre.name,
                city = it.theatre.city,
                startTime = it.startTime.toString(),
                endTime = it.endTime.toString(),
                totalSeats = it.totalSeats,
                availableSeats = it.availableSeats
            )
        }
    }
}

data class ShowResponse(
    val id: Long,
    val movieId: Long,
    val movieTitle: String,
    val theatreId: Long,
    val theatreName: String,
    val city: String,
    val startTime: String,
    val endTime: String,
    val totalSeats: Int,
    val availableSeats: Int
)

data class SeatResponse(
    val id: Long,
    val seatNumber: String,
    val zone: String,
    val status: String
)
