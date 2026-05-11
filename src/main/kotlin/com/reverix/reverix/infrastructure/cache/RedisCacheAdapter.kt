package com.reverix.reverix.infrastructure.cache

import com.reverix.reverix.domain.port.output.CachePort
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class RedisCacheAdapter(
    private val stringRedisTemplate: StringRedisTemplate
) : CachePort {

    override fun lockSeat(seatId: Long, userId: Long, ttlSeconds: Long): Boolean {
        val key = "seat:lock:$seatId"
        val success = stringRedisTemplate.opsForValue()
            .setIfAbsent(key, "userId:$userId", Duration.ofSeconds(ttlSeconds))
        return success ?: false
    }

    override fun unlockSeat(seatId: Long): Boolean {
        val key = "seat:lock:$seatId"
        return stringRedisTemplate.delete(key)
    }

    override fun isSeatLocked(seatId: Long): Boolean {
        val key = "seat:lock:$seatId"
        return stringRedisTemplate.hasKey(key) ?: false
    }
}
