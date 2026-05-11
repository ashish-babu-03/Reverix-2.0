package com.reverix.reverix.infrastructure.persistence

import com.reverix.reverix.domain.model.Booking
import com.reverix.reverix.domain.port.output.BookingRepository
import com.reverix.reverix.infrastructure.persistence.mapper.BookingMapper
import com.reverix.reverix.infrastructure.persistence.repository.BookingJpaRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class BookingRepositoryAdapter(
    private val bookingJpaRepository: BookingJpaRepository,
    private val bookingMapper: BookingMapper
) : BookingRepository {

    @Transactional
    override fun save(booking: Booking): Booking {
        with(bookingMapper) {
            return bookingJpaRepository.save(booking.toEntity()).toDomain()
        }
    }

    @Transactional
    override fun findById(id: Long): Booking? {
        with(bookingMapper) {
            return bookingJpaRepository.findByIdWithDetails(id)?.toDomain()
        }
    }

    @Transactional
    override fun findByIdempotencyKey(key: String): Booking? {
        with(bookingMapper) {
            return bookingJpaRepository.findByIdempotencyKeyWithDetails(key)?.toDomain()
        }
    }

    @Transactional
    override fun findByUserId(userId: Long): List<Booking> {
        with(bookingMapper) {
            return bookingJpaRepository.findByUserIdWithDetails(userId).map { it.toDomain() }
        }
    }
}
