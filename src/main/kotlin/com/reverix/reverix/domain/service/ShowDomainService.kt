package com.reverix.reverix.domain.service

import com.reverix.reverix.domain.model.Seat
import com.reverix.reverix.domain.model.SeatStatus
import com.reverix.reverix.domain.model.Show
import com.reverix.reverix.domain.model.Zone
import com.reverix.reverix.domain.port.input.ManageShowUseCase
import com.reverix.reverix.domain.port.output.SeatRepository
import com.reverix.reverix.domain.port.output.ShowRepository
import java.util.Random

class ShowDomainService(
    private val showRepository: ShowRepository,
    private val seatRepository: SeatRepository
) : ManageShowUseCase {

    override fun getShowsByMovie(movieId: Long): List<Show> {
        return showRepository.findByMovieId(movieId)
    }

    override fun getAvailableSeats(showId: Long): List<Seat> {
        val existingSeats = seatRepository.findByShowId(showId)
        if (existingSeats.isNotEmpty()) {
            return existingSeats
        }

        val show = showRepository.findById(showId) 
            ?: throw IllegalArgumentException("Show not found for ID $showId")

        val generatedSeats = mutableListOf<Seat>()
        val rows = listOf("A", "B", "C", "D", "E", "F", "G")
        val random = Random(showId) // Use showId as seed for consistent randomness

        for (row in rows) {
            val zone = when (row) {
                "A", "B" -> Zone.FRONT // Using FRONT mapping to PREMIUM
                "C", "D", "E" -> Zone.MIDDLE // Using MIDDLE mapping to EXECUTIVE
                else -> Zone.BACK // Using BACK mapping to STANDARD
            }

            for (num in 1..10) {
                val seatNumber = "$row$num"
                val isBooked = random.nextDouble() < 0.3
                val status = if (isBooked) SeatStatus.BOOKED else SeatStatus.AVAILABLE

                generatedSeats.add(
                    Seat(
                        show = show,
                        seatNumber = seatNumber,
                        zone = zone,
                        status = status
                    )
                )
            }
        }

        return seatRepository.saveAll(generatedSeats)
    }
}
