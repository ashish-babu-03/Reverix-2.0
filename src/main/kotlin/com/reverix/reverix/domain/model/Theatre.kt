package com.reverix.reverix.domain.model

data class Theatre(
    val id: Long? = null,
    val name: String,
    val city: String,
    val vibe: Vibe
)

enum class Vibe {
    CELEBRATION, SILENT, FAMILY, DATE_NIGHT, PREMIERE
}
