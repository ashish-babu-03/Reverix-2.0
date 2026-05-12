package com.reverix.reverix.infrastructure.web.rest

import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
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

@Component
class SelfPingScheduler {
    private val logger = LoggerFactory.getLogger(SelfPingScheduler::class.java)
    private val restTemplate = RestTemplate()

    @Scheduled(fixedDelay = 840000) // every 14 minutes
    fun ping() {
        try {
            val response = restTemplate.getForEntity(
                "https://reverix-2-0.onrender.com/api/health",
                String::class.java
            )
            logger.info("Self ping successful: ${response.statusCode}")
        } catch (ex: Exception) {
            logger.error("Self ping failed: ${ex.message}")
        }
    }
}