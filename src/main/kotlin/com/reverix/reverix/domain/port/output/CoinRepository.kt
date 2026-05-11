package com.reverix.reverix.domain.port.output

import com.reverix.reverix.domain.model.CoinTransaction

interface CoinRepository {
    fun save(transaction: CoinTransaction): CoinTransaction
    fun findByUserId(userId: Long): List<CoinTransaction>
}
