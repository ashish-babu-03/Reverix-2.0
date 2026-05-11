package com.reverix.reverix.infrastructure.persistence.entity

import jakarta.persistence.*

@Entity
@Table(name = "movies")
data class MovieEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val title: String,
    val genre: String,
    val moodTags: String,
    val language: String,
    val rating: Double?,
    val duration: Int,
    val posterUrl: String?
)
