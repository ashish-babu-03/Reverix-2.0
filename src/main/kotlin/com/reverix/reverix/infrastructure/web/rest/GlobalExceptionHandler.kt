package com.reverix.reverix.infrastructure.web.rest

import com.reverix.reverix.domain.model.BookingNotFoundException
import com.reverix.reverix.domain.model.SeatAlreadyLockedException
import com.reverix.reverix.domain.model.UnauthorizedBookingException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(SeatAlreadyLockedException::class)
    fun handleSeatAlreadyLocked(e: SeatAlreadyLockedException): ResponseEntity<ErrorResponse> {
        return buildErrorResponse(e.message ?: "Seat already locked", HttpStatus.CONFLICT)
    }

    @ExceptionHandler(BookingNotFoundException::class)
    fun handleBookingNotFound(e: BookingNotFoundException): ResponseEntity<ErrorResponse> {
        return buildErrorResponse(e.message ?: "Booking not found", HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(UnauthorizedBookingException::class)
    fun handleUnauthorizedBooking(e: UnauthorizedBookingException): ResponseEntity<ErrorResponse> {
        return buildErrorResponse(e.message ?: "Unauthorized access to booking", HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(e: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        return buildErrorResponse(e.message ?: "Bad request", HttpStatus.BAD_REQUEST)
    }

    private fun buildErrorResponse(message: String, status: HttpStatus): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            message = message,
            status = status.value(),
            timestamp = LocalDateTime.now()
        )
        return ResponseEntity.status(status).body(errorResponse)
    }
}

data class ErrorResponse(val message: String, val status: Int, val timestamp: LocalDateTime)
