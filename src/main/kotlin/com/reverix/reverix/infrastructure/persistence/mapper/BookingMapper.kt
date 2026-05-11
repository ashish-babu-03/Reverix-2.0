package com.reverix.reverix.infrastructure.persistence.mapper

import com.reverix.reverix.domain.model.*
import com.reverix.reverix.infrastructure.persistence.entity.BookingEntity
import com.reverix.reverix.infrastructure.persistence.repository.ShowJpaRepository
import com.reverix.reverix.infrastructure.persistence.repository.UserJpaRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class BookingMapper(
    private val userJpaRepository: UserJpaRepository,
    private val showJpaRepository: ShowJpaRepository
) {
    fun BookingEntity.toDomain(): Booking {
        val movieDomain = show.movie.let { m ->
            Movie(
                id = m.id,
                title = m.title,
                genre = m.genre,
                moodTags = m.moodTags.split(",").map { it.trim() },
                language = m.language,
                rating = m.rating,
                duration = m.duration,
                posterUrl = m.posterUrl
            )
        }

        val theatreDomain = show.theatre.let { t ->
            Theatre(
                id = t.id,
                name = t.name,
                city = t.city,
                vibe = Vibe.valueOf(t.vibe)
            )
        }

        val showDomain = Show(
            id = show.id,
            movie = movieDomain,
            theatre = theatreDomain,
            startTime = show.startTime,
            endTime = show.endTime,
            totalSeats = show.totalSeats,
            availableSeats = show.availableSeats
        )

        val userDomain = User(
            id = user.id,
            email = user.email,
            passwordHash = user.passwordHash,
            role = Role.valueOf(user.role),
            createdAt = user.createdAt
        )

        val seatsDomain = seats.map { st ->
            Seat(
                id = st.id,
                show = showDomain,
                seatNumber = st.seatNumber,
                zone = Zone.valueOf(st.zone),
                status = SeatStatus.valueOf(st.status)
            )
        }.ifEmpty {
            seatIds.split(",").filter { it.isNotBlank() }
                .mapNotNull { it.toLongOrNull() }
                .map {
                    Seat(
                        id = it,
                        show = showDomain,
                        seatNumber = "",
                        zone = Zone.FRONT,
                        status = SeatStatus.BOOKED
                    )
                }
        }

        return Booking(
            id = id,
            user = userDomain,
            show = showDomain,
            seats = seatsDomain,
            status = BookingStatus.valueOf(status),
            idempotencyKey = idempotencyKey,
            createdAt = createdAt
        )
    }

    fun Booking.toEntity(): BookingEntity {
        val userEntity = userJpaRepository.findById(user.id ?: 0)
            .orElseThrow { IllegalStateException("User not found: ${user.id}") }
        val showEntity = showJpaRepository.findById(show.id ?: 0)
            .orElseThrow { IllegalStateException("Show not found: ${show.id}") }
        return BookingEntity(
            id = id ?: 0,
            user = userEntity,
            show = showEntity,
            seatIds = seats.mapNotNull { it.id }.joinToString(","),
            status = status.name,
            idempotencyKey = idempotencyKey,
            createdAt = createdAt
        )
    }
}