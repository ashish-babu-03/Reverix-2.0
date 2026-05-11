package com.reverix.reverix.infrastructure.persistence

import com.reverix.reverix.domain.model.Booking
import com.reverix.reverix.domain.port.output.BookingRepository
import com.reverix.reverix.infrastructure.persistence.mapper.BookingMapper
import com.reverix.reverix.infrastructure.persistence.repository.BookingJpaRepository
import org.springframework.stereotype.Component

@Component
class BookingRepositoryAdapter(
    private val bookingJpaRepository: BookingJpaRepository,
    private val bookingMapper: BookingMapper
) : BookingRepository {

    override fun save(booking: Booking): Booking {
        with(bookingMapper) {
            return bookingJpaRepository.save(booking.toEntity()).toDomain()
        }
    }

    override fun findById(id: Long): Booking? {
        with(bookingMapper) {
            return bookingJpaRepository.findById(id).orElse(null)?.toDomain()
        }
    }

    override fun findByIdempotencyKey(key: String): Booking? {
        with(bookingMapper) {
            return bookingJpaRepository.findByIdempotencyKey(key)?.toDomain()
        }
    }

    override fun findByUserId(userId: Long): List<Booking> {
        with(bookingMapper) {
            return bookingJpaRepository.findByUserIdWithDetails(userId).map { it.toDomain() }
        }
    }
}
