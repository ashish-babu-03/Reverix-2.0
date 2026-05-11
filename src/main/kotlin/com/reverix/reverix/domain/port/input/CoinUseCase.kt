package com.reverix.reverix.domain.port.input

import com.reverix.reverix.domain.model.CoinTransaction
import com.reverix.reverix.domain.model.TransactionType

interface CoinUseCase {
    fun getBalance(userId: Long): Int
    fun earnCoins(userId: Long, type: TransactionType): CoinTransaction
    fun redeemCoins(userId: Long, amount: Int): CoinTransaction
    fun topupCoins(userId: Long, amount: Int): CoinTransaction
}
