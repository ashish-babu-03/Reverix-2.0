package com.reverix.reverix.domain.port.output

interface CachePort {
    fun lockSeat(seatId: Long, userId: Long, ttlSeconds: Long): Boolean
    fun unlockSeat(seatId: Long): Boolean
    fun isSeatLocked(seatId: Long): Boolean
}
