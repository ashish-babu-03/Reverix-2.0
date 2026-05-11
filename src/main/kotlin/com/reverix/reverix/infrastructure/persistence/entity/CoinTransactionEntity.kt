package com.reverix.reverix.infrastructure.persistence.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "coin_transactions")
data class CoinTransactionEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val userId: Long,
    val amount: Int,
    val type: String,
    val description: String? = null,
    val createdAt: LocalDateTime
)
