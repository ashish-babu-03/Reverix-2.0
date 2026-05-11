package com.reverix.reverix.infrastructure.ai

import com.reverix.reverix.domain.model.Movie
import com.reverix.reverix.domain.port.output.AIPort
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class OpenRouterAIAdapter(
    webClientBuilder: WebClient.Builder,
    @Value("\${openrouter.api.key:dummy}") private val apiKey: String
) : AIPort {

    private val webClient = webClientBuilder.baseUrl("https://openrouter.ai/api/v1").build()

    @CircuitBreaker(name = "openrouter", fallbackMethod = "fallbackRecommendations")
    override fun getRecommendations(mood: String, movies: List<Movie>): List<Movie> {
        val movieListStr = movies.joinToString { it.title }
        val prompt = "I am in a $mood mood. Recommend some movies from this list: $movieListStr. Return only a comma-separated list of titles."

        val requestBody = mapOf(
            "model" to "meta-llama/llama-3.3-70b-instruct",
            "messages" to listOf(
                mapOf("role" to "user", "content" to prompt)
            )
        )

        try {
            val response = webClient.post()
                .uri("/chat/completions")
                .header("Authorization", "Bearer $apiKey")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map::class.java)
                .block()

            val choices = response?.get("choices") as? List<Map<String, Any>>
            val message = choices?.firstOrNull()?.get("message") as? Map<String, String>
            val content = message?.get("content") ?: return emptyList()

            val recommendedTitles = content.split(",").map { it.trim() }
            return movies.filter { recommendedTitles.any { title -> it.title.contains(title, ignoreCase = true) } }
        } catch (e: Exception) {
            throw RuntimeException("Failed to get recommendations from AI", e)
        }
    }

    override fun chat(userMessage: String, context: String): String {
        val requestBody = mapOf(
            "model" to "meta-llama/llama-3.3-70b-instruct",
            "messages" to listOf(
                mapOf("role" to "system", "content" to context),
                mapOf("role" to "user", "content" to userMessage)
            )
        )

        val response = webClient.post()
            .uri("/chat/completions")
            .header("Authorization", "Bearer $apiKey")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(Map::class.java)
            .block()

        val choices = response?.get("choices") as? List<Map<String, Any>>
        val message = choices?.firstOrNull()?.get("message") as? Map<String, String>
        return message?.get("content") ?: "No response"
    }

    fun fallbackRecommendations(mood: String, movies: List<Movie>, t: Throwable): List<Movie> {
        return movies.take(5)
    }
}
