package com.reverix.reverix.infrastructure.persistence

import com.reverix.reverix.domain.model.User
import com.reverix.reverix.domain.port.output.UserRepository
import com.reverix.reverix.infrastructure.persistence.mapper.UserMapper
import com.reverix.reverix.infrastructure.persistence.repository.UserJpaRepository
import org.springframework.stereotype.Component

@Component
class UserRepositoryAdapter(
    private val userJpaRepository: UserJpaRepository,
    private val userMapper: UserMapper
) : UserRepository {

    override fun save(user: User): User {
        with(userMapper) {
            return userJpaRepository.save(user.toEntity()).toDomain()
        }
    }

    override fun findByEmail(email: String): User? {
        with(userMapper) {
            return userJpaRepository.findByEmail(email)?.toDomain()
        }
    }

    override fun findById(id: Long): User? {
        with(userMapper) {
            return userJpaRepository.findById(id).orElse(null)?.toDomain()
        }
    }
}
