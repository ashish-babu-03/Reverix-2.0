package com.reverix.reverix.infrastructure.web.rest

import com.reverix.reverix.domain.port.input.ManageShowUseCase
import org.springframework.web.bind.annotation.*

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
}

data class SeatResponse(
    val id: Long,
    val seatNumber: String,
    val zone: String,
    val status: String
)
