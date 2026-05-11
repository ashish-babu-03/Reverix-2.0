package com.reverix.reverix.infrastructure.persistence.adapter

import com.reverix.reverix.domain.model.CoinTransaction
import com.reverix.reverix.domain.port.output.CoinRepository
import com.reverix.reverix.infrastructure.persistence.mapper.toDomain
import com.reverix.reverix.infrastructure.persistence.mapper.toEntity
import com.reverix.reverix.infrastructure.persistence.repository.CoinTransactionJpaRepository
import org.springframework.stereotype.Component

@Component
class CoinRepositoryAdapter(
    private val coinTransactionJpaRepository: CoinTransactionJpaRepository
) : CoinRepository {

    override fun save(transaction: CoinTransaction): CoinTransaction {
        return coinTransactionJpaRepository.save(transaction.toEntity()).toDomain()
    }

    override fun findByUserId(userId: Long): List<CoinTransaction> {
        return coinTransactionJpaRepository.findByUserId(userId).map { it.toDomain() }
    }
}
