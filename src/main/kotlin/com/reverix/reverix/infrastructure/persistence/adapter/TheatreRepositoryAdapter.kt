package com.reverix.reverix.infrastructure.persistence.adapter

import com.reverix.reverix.domain.model.Theatre
import com.reverix.reverix.domain.port.output.TheatreRepository
import com.reverix.reverix.infrastructure.persistence.mapper.toDomain
import com.reverix.reverix.infrastructure.persistence.repository.TheatreJpaRepository
import org.springframework.stereotype.Component

@Component
class TheatreRepositoryAdapter(
    private val theatreJpaRepository: TheatreJpaRepository
) : TheatreRepository {

    override fun findById(id: Long): Theatre? {
        return theatreJpaRepository.findById(id).orElse(null)?.toDomain()
    }
}
