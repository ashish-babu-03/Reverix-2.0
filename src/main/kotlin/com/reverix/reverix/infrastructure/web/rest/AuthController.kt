package com.reverix.reverix.infrastructure.web.rest

import com.reverix.reverix.infrastructure.persistence.entity.UserEntity
import com.reverix.reverix.infrastructure.persistence.repository.UserJpaRepository
import com.reverix.reverix.infrastructure.security.JwtUtil
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userJpaRepository: UserJpaRepository,
    private val jwtUtil: JwtUtil,
    private val passwordEncoder: PasswordEncoder
) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<Any> {
        if (userJpaRepository.findByEmail(request.email) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to "Email already exists"))
        }

        val userEntity = UserEntity(
            email = request.email,
            passwordHash = passwordEncoder.encode(request.password),
            role = request.role,
            createdAt = LocalDateTime.now()
        )
        userJpaRepository.save(userEntity)

        val token = jwtUtil.generateToken(userEntity.email, userEntity.role)
        return ResponseEntity.ok(AuthResponse(token, userEntity.role))
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<Any> {
        val user = userJpaRepository.findByEmail(request.email)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("error" to "Invalid credentials"))

        if (!passwordEncoder.matches(request.password, user.passwordHash)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("error" to "Invalid credentials"))
        }

        val token = jwtUtil.generateToken(user.email, user.role)
        return ResponseEntity.ok(AuthResponse(token, user.role))
    }
}

data class RegisterRequest(val email: String, val password: String, val role: String = "USER")
data class LoginRequest(val email: String, val password: String)
data class AuthResponse(val token: String, val role: String? = null)
