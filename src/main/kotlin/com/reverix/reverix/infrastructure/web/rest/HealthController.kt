package com.reverix.reverix.infrastructure.web.rest

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.sql.DataSource

@RestController
class HealthController(
    private val dataSource: DataSource,
    private val redisTemplate: RedisTemplate<String, String>
) {
    @GetMapping("/api/health")
    fun checkHealth(): Map<String, Any> {
        val dbStatus = try {
            // Checks if Postgres connection is alive
            dataSource.connection.use { if (it.isValid(1)) "UP" else "DOWN" }
        } catch (e: Exception) { "DOWN" }

        val redisStatus = try {
            // Checks if Redis is available for atomic seat locks
            val ping = redisTemplate.connectionFactory?.connection?.ping()
            if (ping == "PONG") "UP" else "DOWN"
        } catch (e: Exception) { "DOWN" }

        return mapOf(
            "status" to if (dbStatus == "UP" && redisStatus == "UP") "UP" else "DEGRADED",
            "database" to dbStatus,
            "redis" to redisStatus,
            "timestamp" to System.currentTimeMillis()
        )
    }
}