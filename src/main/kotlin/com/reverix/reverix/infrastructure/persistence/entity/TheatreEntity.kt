package com.reverix.reverix.infrastructure.persistence.entity

import com.reverix.reverix.domain.model.Vibe
import jakarta.persistence.*

@Entity
@Table(name = "theatres")
data class TheatreEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    val city: String,
    val vibe: String
)
