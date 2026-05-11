package com.reverix.reverix.infrastructure.persistence.repository

import com.reverix.reverix.infrastructure.persistence.entity.CoinTransactionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CoinTransactionJpaRepository : JpaRepository<CoinTransactionEntity, Long> {
    fun findByUserId(userId: Long): List<CoinTransactionEntity>
}
