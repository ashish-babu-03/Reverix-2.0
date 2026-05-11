package com.reverix.reverix.domain.model

data class Movie(
    val id: Long? = null,
    val title: String,
    val genre: String,
    val moodTags: List<String>,
    val language: String,
    val rating: Double?,
    val duration: Int,
    val posterUrl: String?
)
