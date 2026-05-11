package com.reverix.reverix.infrastructure.persistence.mapper

import com.reverix.reverix.domain.model.Role
import com.reverix.reverix.domain.model.User
import com.reverix.reverix.infrastructure.persistence.entity.UserEntity
import org.springframework.stereotype.Component

@Component
class UserMapper {
    fun UserEntity.toDomain() = User(
        id = id,
        email = email,
        passwordHash = passwordHash,
        role = Role.valueOf(role),
        createdAt = createdAt
    )

    fun User.toEntity() = UserEntity(
        id = id ?: 0,
        email = email,
        passwordHash = passwordHash,
        role = role.name,
        createdAt = createdAt
    )
}
