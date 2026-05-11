package com.reverix.reverix.application.usecase

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class BookingEventListener {

    private val logger = LoggerFactory.getLogger(BookingEventListener::class.java)

    @EventListener
    fun onSeatLocked(event: SeatLockedEvent) {
        logger.info("Seats locked for booking {} by user {}", event.bookingId, event.userId)
        // Future: trigger email/push notification here
    }

    @EventListener
    fun onBookingConfirmed(event: BookingConfirmedEvent) {
        logger.info("Booking {} confirmed for user {}", event.bookingId, event.userId)
        // Future: trigger invoice generation here
    }
}
