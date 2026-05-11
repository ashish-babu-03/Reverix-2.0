package com.reverix.reverix.infrastructure.config

import com.reverix.reverix.domain.port.output.AIPort
import com.reverix.reverix.domain.port.output.BookingRepository
import com.reverix.reverix.domain.port.output.CachePort
import com.reverix.reverix.domain.port.output.MovieRepository
import com.reverix.reverix.domain.port.output.SeatRepository
import com.reverix.reverix.domain.port.output.ShowRepository
import com.reverix.reverix.domain.service.BookingDomainService
import com.reverix.reverix.domain.service.MovieDomainService
import com.reverix.reverix.domain.service.RecommendationDomainService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class AppConfig {

    @Bean
    fun webClientBuilder(): WebClient.Builder {
        return WebClient.builder()
    }

    @Bean
    fun redisTemplate(connectionFactory: RedisConnectionFactory): StringRedisTemplate {
        return StringRedisTemplate(connectionFactory)
    }

    @Bean
    fun bookingDomainService(
        bookingRepository: BookingRepository,
        seatRepository: SeatRepository,
        showRepository: ShowRepository,
        cachePort: CachePort
    ): BookingDomainService {
        return BookingDomainService(bookingRepository, seatRepository, showRepository, cachePort)
    }

    @Bean
    fun movieDomainService(movieRepository: MovieRepository): MovieDomainService {
        return MovieDomainService(movieRepository)
    }

    @Bean
    fun recommendationDomainService(
        movieRepository: MovieRepository,
        aiPort: AIPort
    ): RecommendationDomainService {
        return RecommendationDomainService(movieRepository, aiPort)
    }

    @Bean
    fun reviewDomainService(reviewRepository: com.reverix.reverix.domain.port.output.ReviewRepository) = 
        com.reverix.reverix.domain.service.ReviewDomainService(reviewRepository)

    @Bean
    fun watchlistDomainService(watchlistRepository: com.reverix.reverix.domain.port.output.WatchlistRepository) = 
        com.reverix.reverix.domain.service.WatchlistDomainService(watchlistRepository)

    @Bean
    fun coinDomainService(coinRepository: com.reverix.reverix.domain.port.output.CoinRepository) = 
        com.reverix.reverix.domain.service.CoinDomainService(coinRepository)
    @Bean
    fun showDomainService(
        showRepository: com.reverix.reverix.domain.port.output.ShowRepository,
        seatRepository: com.reverix.reverix.domain.port.output.SeatRepository
    ) = com.reverix.reverix.domain.service.ShowDomainService(showRepository, seatRepository)
}
