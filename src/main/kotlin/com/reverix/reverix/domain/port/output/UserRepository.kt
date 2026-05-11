package com.reverix.reverix.domain.port.output

import com.reverix.reverix.domain.model.User

interface UserRepository {
    fun save(user: User): User
    fun findByEmail(email: String): User?
    fun findById(id: Long): User?
}
