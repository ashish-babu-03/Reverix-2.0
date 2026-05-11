package com.reverix.reverix.infrastructure.web.rest

import com.reverix.reverix.domain.model.CoinTransaction
import com.reverix.reverix.domain.model.TransactionType
import com.reverix.reverix.domain.port.input.CoinUseCase
import com.reverix.reverix.infrastructure.persistence.repository.UserJpaRepository
import com.reverix.reverix.infrastructure.security.JwtUtil
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/coins")
class CoinController(
    private val coinUseCase: CoinUseCase,
    private val userJpaRepository: UserJpaRepository,
    private val jwtUtil: JwtUtil
) {

    private fun getUserIdFromToken(authHeader: String): Long {
        val token = authHeader.substring(7)
        val email = jwtUtil.extractEmail(token)
        val user = userJpaRepository.findByEmail(email) 
            ?: throw IllegalArgumentException("User not found")
        return user.id
    }

    @GetMapping("/balance")
    fun getBalance(
        @RequestHeader("Authorization") authHeader: String
    ): Map<String, Int> {
        val userId = getUserIdFromToken(authHeader)
        return mapOf("balance" to coinUseCase.getBalance(userId))
    }

    @PostMapping("/earn")
    fun earnCoins(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody request: EarnCoinsRequest
    ): CoinTransaction {
        val userId = getUserIdFromToken(authHeader)
        return coinUseCase.earnCoins(userId, TransactionType.valueOf(request.type))
    }

    @PostMapping("/redeem")
    fun redeemCoins(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody request: RedeemCoinsRequest
    ): CoinTransaction {
        val userId = getUserIdFromToken(authHeader)
        return coinUseCase.redeemCoins(userId, request.amount)
    }

    @PostMapping("/topup")
    fun topupCoins(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody request: TopupCoinsRequest
    ): Map<String, Int> {
        val userId = getUserIdFromToken(authHeader)
        coinUseCase.topupCoins(userId, request.amount)
        return mapOf("balance" to coinUseCase.getBalance(userId))
    }
}

data class EarnCoinsRequest(val type: String)
data class RedeemCoinsRequest(val amount: Int)
data class TopupCoinsRequest(val amount: Int, val upiRef: String)
