package com.reverix.reverix.domain.model

class SeatAlreadyLockedException(seatId: Long) : RuntimeException("Seat $seatId is already locked")
class BookingNotFoundException(id: Long) : RuntimeException("Booking with ID $id not found")
class UnauthorizedBookingException(userId: Long) : RuntimeException("User $userId is not authorized for this booking")
class UnauthorizedBookingEmailException(email: String) : RuntimeException("User $email is not authorized for this booking")
class SeatNotAvailableException(seatId: Long) : RuntimeException("Seat $seatId is not available")
