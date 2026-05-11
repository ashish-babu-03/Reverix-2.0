package com.reverix.reverix.infrastructure.persistence

import com.reverix.reverix.domain.model.Show
import com.reverix.reverix.domain.port.output.ShowRepository
import com.reverix.reverix.infrastructure.persistence.mapper.ShowMapper
import com.reverix.reverix.infrastructure.persistence.repository.ShowJpaRepository
import org.springframework.stereotype.Component

@Component
class ShowRepositoryAdapter(
    private val showJpaRepository: ShowJpaRepository,
    private val showMapper: ShowMapper
) : ShowRepository {

    override fun findById(id: Long): Show? {
        with(showMapper) {
            return showJpaRepository.findById(id).orElse(null)?.toDomain()
        }
    }

    override fun findByMovieId(movieId: Long): List<Show> {
        with(showMapper) {
            return showJpaRepository.findByMovieId(movieId).map { it.toDomain() }
        }
    }

    override fun findByTheatreIdAndMovieId(theatreId: Long, movieId: Long): List<Show> {
        with(showMapper) {
            return showJpaRepository.findByTheatreIdAndMovieId(theatreId, movieId).map { it.toDomain() }
        }
    }

    override fun save(show: Show): Show {
        with(showMapper) {
            val entity = show.toEntity()
            return showJpaRepository.save(entity).toDomain()
        }
    }
}
