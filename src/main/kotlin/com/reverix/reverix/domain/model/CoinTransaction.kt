package com.reverix.reverix.domain.model

import java.time.LocalDateTime

data class CoinTransaction(
    val id: Long = 0,
    val userId: Long,
    val amount: Int,
    val type: TransactionType,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

enum class TransactionType {
    BOOKING,
    REVIEW,
    AD,
    REDEEM,
    TOPUP
}
