package com.reverix.reverix.infrastructure.config

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.reverix.reverix.infrastructure.persistence.entity.*
import com.reverix.reverix.infrastructure.persistence.repository.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class TmdbPageResponse(
    val page: Int,
    val results: List<TmdbMovie>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class TmdbMovie(
    val id: Long,
    val title: String,
    val overview: String?,
    @JsonProperty("genre_ids") val genreIds: List<Int>,
    @JsonProperty("original_language") val originalLanguage: String,
    @JsonProperty("vote_average") val voteAverage: Double,
    @JsonProperty("poster_path") val posterPath: String?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class TmdbMovieDetail(
    val id: Long,
    val runtime: Int?
)

@Component
class TmdbDataSeeder(
    private val movieJpaRepository: MovieJpaRepository,
    private val theatreJpaRepository: TheatreJpaRepository,
    private val showJpaRepository: ShowJpaRepository,
    private val seatJpaRepository: SeatJpaRepository,
    webClientBuilder: WebClient.Builder,
    @Value("\${tmdb.api.key}") private val tmdbApiKey: String,
    @Value("\${tmdb.api.base-url}") private val tmdbBaseUrl: String,
    @Value("\${tmdb.api.poster-base-url}") private val tmdbPosterBaseUrl: String
) : ApplicationListener<ApplicationReadyEvent> {

    private val logger = LoggerFactory.getLogger(TmdbDataSeeder::class.java)
    private val webClient = webClientBuilder.baseUrl(tmdbBaseUrl).build()

    private val genreMap = mapOf(
        28 to ("Action" to listOf("excited", "thrilling", "intense", "energetic")),
        12 to ("Adventure" to listOf("excited", "epic", "thrilling", "adventurous")),
        16 to ("Animation" to listOf("happy", "light", "feel-good", "family")),
        35 to ("Comedy" to listOf("funny", "happy", "light", "feel-good")),
        80 to ("Crime" to listOf("intense", "dark", "thrilling", "serious")),
        99 to ("Documentary" to listOf("thoughtful", "informative", "serious")),
        18 to ("Drama" to listOf("emotional", "thoughtful", "heartwarming", "sad")),
        10751 to ("Family" to listOf("happy", "light", "feel-good", "family")),
        14 to ("Fantasy" to listOf("magical", "epic", "excited", "adventurous")),
        36 to ("History" to listOf("thoughtful", "serious", "epic", "informative")),
        27 to ("Horror" to listOf("scared", "intense", "dark", "thrilling")),
        10402 to ("Music" to listOf("happy", "feel-good", "emotional", "light")),
        9648 to ("Mystery" to listOf("mind-bending", "intense", "curious", "dark")),
        10749 to ("Romance" to listOf("romantic", "emotional", "happy", "nostalgic")),
        878 to ("Sci-Fi" to listOf("mind-bending", "excited", "epic", "thoughtful")),
        53 to ("Thriller" to listOf("intense", "thrilling", "excited", "dark")),
        10752 to ("War" to listOf("intense", "emotional", "epic", "serious")),
        37 to ("Western" to listOf("adventurous", "intense", "thrilling"))
    )

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        Thread {
            try {
                if (movieJpaRepository.count() > 0) {
                    logger.info("Data already seeded, skipping...")
                    return@Thread
                }
                logger.info("Fetching real movie data from TMDb in background...")
                seedMoviesFromTmdb()
                seedTheatres()
                seedShowsAndSeats()
                logger.info("Background seeding complete! {} movies loaded.", movieJpaRepository.count())
            } catch (e: Exception) {
                logger.error("Data seeding failed: ${e.message}")
            }
        }.start()
    }

    private fun seedMoviesFromTmdb() {
        val endpoints = listOf(
            "/movie/now_playing?api_key=$tmdbApiKey&language=en-US&page=1",
            "/movie/now_playing?api_key=$tmdbApiKey&language=en-US&page=2",
            "/movie/now_playing?api_key=$tmdbApiKey&language=en-US&page=3",
            "/movie/popular?api_key=$tmdbApiKey&language=en-US&page=1",
            "/movie/popular?api_key=$tmdbApiKey&language=en-US&page=2",
            "/movie/top_rated?api_key=$tmdbApiKey&language=en-US&page=1"
        )

        val fetchedMovies = mutableListOf<MovieEntity>()
        val seenTitles = mutableSetOf<String>()

        for (endpoint in endpoints) {
            val response = webClient.get()
                .uri(endpoint)
                .retrieve()
                .bodyToMono(TmdbPageResponse::class.java)
                .block()

            response?.results?.forEach { tmdbMovie ->
                if (tmdbMovie.posterPath == null) return@forEach
                if (seenTitles.contains(tmdbMovie.title)) return@forEach

                try {
                    Thread.sleep(100)
                    val detail = webClient.get()
                        .uri("/movie/${tmdbMovie.id}?api_key=$tmdbApiKey")
                        .retrieve()
                        .bodyToMono(TmdbMovieDetail::class.java)
                        .block()

                    val runtime = detail?.runtime ?: 120
                    val actualRuntime = if (runtime == 0) 120 else runtime

                    val tags = tmdbMovie.genreIds
                        .flatMap { genreMap[it]?.second ?: emptyList() }
                        .toSet().joinToString(",")
                    val genreName = tmdbMovie.genreIds.firstOrNull()
                        ?.let { genreMap[it]?.first } ?: "Unknown"

                    fetchedMovies.add(MovieEntity(
                        title = tmdbMovie.title,
                        genre = genreName,
                        moodTags = tags.ifEmpty { "thoughtful" },
                        language = tmdbMovie.originalLanguage,
                        rating = tmdbMovie.voteAverage,
                        duration = actualRuntime,
                        posterUrl = "$tmdbPosterBaseUrl${tmdbMovie.posterPath}"
                    ))
                    seenTitles.add(tmdbMovie.title)

                } catch (e: Exception) {
                    logger.warn("Failed to fetch details for movie ${tmdbMovie.title}, skipping.")
                }
            }
        }

        if (fetchedMovies.isEmpty()) throw RuntimeException("No movies fetched from TMDb")
        movieJpaRepository.saveAll(fetchedMovies)
        logger.info("Fetched and saved ${fetchedMovies.size} movies from TMDb")
    }

    private fun seedTheatres() {
        if (theatreJpaRepository.count() > 0) return
        val theatres = listOf(
            TheatreEntity(name = "PVR Phoenix MarketCity", city = "Chennai", vibe = "PREMIERE"),
            TheatreEntity(name = "Rohini Silver Screens", city = "Chennai", vibe = "CELEBRATION"),
            TheatreEntity(name = "Sathyam Cinemas", city = "Chennai", vibe = "DATE_NIGHT"),
            TheatreEntity(name = "PVR Forum Mall", city = "Bangalore", vibe = "PREMIERE"),
            TheatreEntity(name = "Inox Garuda Mall", city = "Bangalore", vibe = "FAMILY"),
            TheatreEntity(name = "PVR Juhu", city = "Mumbai", vibe = "DATE_NIGHT")
        )
        theatreJpaRepository.saveAll(theatres)
    }

    private fun seedShowsAndSeats() {
        if (showJpaRepository.count() > 0) return

        val movies = movieJpaRepository.findAll()
        val theatres = theatreJpaRepository.findAll()

        val showsToSave = mutableListOf<ShowEntity>()
        val tomorrow = LocalDateTime.now().plusDays(1)

        movies.forEach { movie ->
            val cityTheatres = theatres.groupBy { it.city }
            val firstCityTheatres = cityTheatres.values.firstOrNull() ?: theatres

            val t1 = firstCityTheatres.getOrNull(0)
            val t2 = firstCityTheatres.getOrNull(1)

            if (t1 != null) {
                showsToSave.add(ShowEntity(
                    movie = movie,
                    theatre = t1,
                    startTime = tomorrow.withHour(10).withMinute(0).withSecond(0),
                    endTime = tomorrow.withHour(10).withMinute(0).withSecond(0)
                        .plusMinutes(movie.duration.toLong()),
                    totalSeats = 100,
                    availableSeats = 100
                ))
            }

            if (t2 != null) {
                showsToSave.add(ShowEntity(
                    movie = movie,
                    theatre = t2,
                    startTime = tomorrow.withHour(18).withMinute(0).withSecond(0),
                    endTime = tomorrow.withHour(18).withMinute(0).withSecond(0)
                        .plusMinutes(movie.duration.toLong()),
                    totalSeats = 100,
                    availableSeats = 100
                ))
            }
        }

        val savedShows = showJpaRepository.saveAll(showsToSave)

        val seatsToSave = mutableListOf<SeatEntity>()
        val rows = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J")

        savedShows.forEach { show ->
            rows.forEach { row ->
                for (i in 1..10) {
                    val zone = when (row) {
                        "A", "B", "C" -> "FRONT"
                        "D", "E", "F", "G" -> "MIDDLE"
                        else -> "BACK"
                    }
                    seatsToSave.add(SeatEntity(
                        show = show,
                        seatNumber = "$row$i",
                        zone = zone,
                        status = "AVAILABLE"
                    ))
                }
            }
        }

        seatJpaRepository.saveAll(seatsToSave)
        logger.info("Seeded ${savedShows.size} shows and ${seatsToSave.size} seats")
    }
}