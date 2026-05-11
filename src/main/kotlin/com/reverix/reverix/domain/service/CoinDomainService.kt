package com.reverix.reverix.domain.service

import com.reverix.reverix.domain.model.CoinTransaction
import com.reverix.reverix.domain.model.TransactionType
import com.reverix.reverix.domain.port.input.CoinUseCase
import com.reverix.reverix.domain.port.output.CoinRepository

class CoinDomainService(
    private val coinRepository: CoinRepository
) : CoinUseCase {

    override fun getBalance(userId: Long): Int {
        val transactions = coinRepository.findByUserId(userId)
        return transactions.sumOf { 
            if (it.type == TransactionType.REDEEM) -it.amount else it.amount 
        }
    }

    override fun earnCoins(userId: Long, type: TransactionType): CoinTransaction {
        require(type != TransactionType.REDEEM) { "Cannot earn coins through REDEEM type" }
        
        val amount = when (type) {
            TransactionType.REVIEW -> 10
            TransactionType.AD -> 5
            TransactionType.BOOKING -> 20
            else -> 0
        }
        
        val transaction = CoinTransaction(
            userId = userId,
            amount = amount,
            type = type
        )
        return coinRepository.save(transaction)
    }

    override fun redeemCoins(userId: Long, amount: Int): CoinTransaction {
        require(amount > 0) { "Redeem amount must be positive" }
        val currentBalance = getBalance(userId)
        require(currentBalance >= amount) { "Insufficient coin balance" }
        
        val transaction = CoinTransaction(
            userId = userId,
            amount = amount,
            type = TransactionType.REDEEM
        )
        return coinRepository.save(transaction)
    }

    override fun topupCoins(userId: Long, amount: Int): CoinTransaction {
        require(amount > 0) { "Topup amount must be positive" }
        val transaction = CoinTransaction(
            userId = userId,
            amount = amount,
            type = TransactionType.TOPUP
        )
        return coinRepository.save(transaction)
    }
}
