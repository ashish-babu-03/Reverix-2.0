package com.reverix.reverix.domain.model

import java.time.LocalDateTime

data class User(
    val id: Long? = null,
    val email: String,
    val passwordHash: String,
    val role: Role,
    val createdAt: LocalDateTime
)

enum class Role {
    USER, ADMIN, CINEPRIME
}
