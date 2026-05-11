package com.reverix.reverix.infrastructure.persistence.mapper

import com.reverix.reverix.domain.model.Theatre
import com.reverix.reverix.domain.model.Vibe
import com.reverix.reverix.infrastructure.persistence.entity.TheatreEntity

fun TheatreEntity.toDomain(): Theatre {
    return Theatre(
        id = id,
        name = name,
        city = city,
        vibe = Vibe.valueOf(vibe)
    )
}

fun Theatre.toEntity(): TheatreEntity {
    return TheatreEntity(
        id = id ?: 0,
        name = name,
        city = city,
        vibe = vibe.name
    )
}
