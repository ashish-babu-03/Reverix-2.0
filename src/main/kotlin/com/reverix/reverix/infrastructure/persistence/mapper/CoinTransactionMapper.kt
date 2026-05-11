package com.reverix.reverix.infrastructure.persistence.mapper

import com.reverix.reverix.domain.model.CoinTransaction
import com.reverix.reverix.domain.model.TransactionType
import com.reverix.reverix.infrastructure.persistence.entity.CoinTransactionEntity

fun CoinTransactionEntity.toDomain(): CoinTransaction {
    return CoinTransaction(
        id = id,
        userId = userId,
        amount = amount,
        type = TransactionType.valueOf(type),
        createdAt = createdAt
    )
}

fun CoinTransaction.toEntity(): CoinTransactionEntity {
    return CoinTransactionEntity(
        id = id,
        userId = userId,
        amount = amount,
        type = type.name,
        createdAt = createdAt
    )
}
