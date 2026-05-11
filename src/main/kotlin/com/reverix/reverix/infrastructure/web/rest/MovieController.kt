package com.reverix.reverix.infrastructure.web.rest

import com.reverix.reverix.domain.model.Movie
import com.reverix.reverix.domain.port.input.GetMovieUseCase
import com.reverix.reverix.domain.port.input.ManageShowUseCase
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/movies")
class MovieController(
    private val getMovieUseCase: GetMovieUseCase,
    private val manageShowUseCase: ManageShowUseCase
) {

    @GetMapping
    fun getAllMovies(): List<Movie> {
        return getMovieUseCase.getAllMovies()
    }

    @GetMapping("/{id}")
    fun getMovieById(@PathVariable id: Long): Movie {
        return getMovieUseCase.getMovieById(id)
    }

    @GetMapping("/mood")
    fun getMoviesByMood(@RequestParam mood: String): List<Movie> {
        return getMovieUseCase.getMoviesByMood(mood)
    }

    @GetMapping("/{id}/theatres")
    fun getTheatresForMovie(@PathVariable id: Long): List<TheatreResponse> {
        val shows = manageShowUseCase.getShowsByMovie(id)
        val showsByTheatre = shows.groupBy { it.theatre }

        return showsByTheatre.map { (theatre, theatreShows) ->
            val screenType = when (theatre.vibe.name) {
                "PREMIERE" -> "IMAX"
                "CELEBRATION", "DATE_NIGHT" -> "DOLBY"
                "FAMILY" -> "STANDARD"
                else -> "STANDARD"
            }

            val snackPrice = when (theatre.vibe.name) {
                "PREMIERE" -> "PREMIUM"
                "FAMILY" -> "BUDGET"
                else -> "MID"
            }

            val ticketPrice = when (screenType) {
                "IMAX" -> 380
                "4DX" -> 350
                "DOLBY" -> 280
                else -> 180
            }

            val distance = when {
                theatre.name.contains("PVR Phoenix") -> "3.2km"
                theatre.name.contains("Rohini") -> "9.3km"
                theatre.name.contains("Sathyam") -> "7.8km"
                theatre.name.contains("AGS") -> "6.5km"
                theatre.name.contains("INOX") -> "4.1km"
                else -> "5.0km"
            }

            TheatreResponse(
                id = theatre.id ?: 0,
                name = theatre.name,
                city = theatre.city,
                vibe = theatre.vibe.name,
                screenType = screenType,
                distance = distance,
                snackPrice = snackPrice,
                ticketPrice = ticketPrice,
                shows = theatreShows.map {
                    ShowSummaryResponse(
                        id = it.id ?: 0,
                        startTime = it.startTime,
                        endTime = it.endTime,
                        availableSeats = it.availableSeats
                    )
                }
            )
        }
    }
}

data class TheatreResponse(
    val id: Long,
    val name: String,
    val city: String,
    val vibe: String,
    val screenType: String,
    val distance: String,
    val snackPrice: String,
    val ticketPrice: Int,
    val shows: List<ShowSummaryResponse>
)

data class ShowSummaryResponse(
    val id: Long,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val availableSeats: Int
)
