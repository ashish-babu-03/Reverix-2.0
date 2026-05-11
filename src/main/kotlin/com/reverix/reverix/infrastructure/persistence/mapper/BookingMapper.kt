package com.reverix.reverix.infrastructure.persistence.mapper

import com.reverix.reverix.domain.model.*
import com.reverix.reverix.infrastructure.persistence.entity.BookingEntity
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class BookingMapper {
    fun BookingEntity.toDomain(): Booking {
        val showDomain = show?.let { s ->
            val movieDomain = s.movie?.let { m ->
                Movie(
                    id = m.id,
                    title = m.title,
                    genre = m.genre,
                    moodTags = m.moodTags.split(","),
                    language = m.language,
                    rating = m.rating,
                    duration = m.duration,
                    posterUrl = m.posterUrl
                )
            } ?: Movie(title = "", genre = "", moodTags = emptyList(), language = "", rating = null, duration = 0, posterUrl = null)

            val theatreDomain = s.theatre?.let { t ->
                Theatre(
                    id = t.id,
                    name = t.name,
                    city = t.city,
                    vibe = Vibe.valueOf(t.vibe)
                )
            } ?: Theatre(name = "", city = "", vibe = Vibe.SILENT)

            Show(
                id = s.id,
                movie = movieDomain,
                theatre = theatreDomain,
                startTime = s.startTime,
                endTime = s.endTime,
                totalSeats = s.totalSeats,
                availableSeats = s.availableSeats
            )
        } ?: Show(id = showId, movie = Movie(title = "", genre = "", moodTags = emptyList(), language = "", rating = null, duration = 0, posterUrl = null), theatre = Theatre(name = "", city = "", vibe = Vibe.SILENT), startTime = LocalDateTime.now(), endTime = LocalDateTime.now(), totalSeats = 0, availableSeats = 0)

        return Booking(
            id = id,
            user = user?.let { u ->
                User(id = u.id, email = u.email, passwordHash = u.passwordHash, role = Role.valueOf(u.role), createdAt = u.createdAt)
            } ?: User(id = userId, email = "", passwordHash = "", role = Role.USER, createdAt = LocalDateTime.now()),
            show = showDomain,
            seats = seats.map { st ->
                Seat(
                    id = st.id,
                    show = showDomain,
                    seatNumber = st.seatNumber,
                    zone = Zone.valueOf(st.zone),
                    status = SeatStatus.valueOf(st.status)
                )
            }.ifEmpty {
                seatIds.split(",").filter { it.isNotBlank() }.mapNotNull { it.toLongOrNull() }.map {
                    Seat(id = it, show = showDomain, seatNumber = "", zone = Zone.FRONT, status = SeatStatus.BOOKED)
                }
            },
            status = BookingStatus.valueOf(status),
            idempotencyKey = idempotencyKey,
            createdAt = createdAt
        )
    }

    fun Booking.toEntity() = BookingEntity(
        id = id ?: 0,
        userId = user.id ?: 0,
        showId = show.id ?: 0,
        seatIds = seats.mapNotNull { it.id }.joinToString(","),
        status = status.name,
        idempotencyKey = idempotencyKey,
        createdAt = createdAt
    )
}
