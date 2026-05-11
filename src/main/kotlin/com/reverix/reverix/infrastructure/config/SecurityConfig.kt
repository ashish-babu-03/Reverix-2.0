package com.reverix.reverix.infrastructure.config

import com.reverix.reverix.infrastructure.security.JwtAuthFilter
import jakarta.servlet.DispatcherType
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { cors ->
                cors.configurationSource {
                    val config = org.springframework.web.cors.CorsConfiguration()
                    config.allowedOrigins = listOf("*")
                    config.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    config.allowedHeaders = listOf("*")
                    config.allowCredentials = false
                    config
                }
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling { exceptions ->
                exceptions.authenticationEntryPoint { _, response, authException ->
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: " + authException.message)
                }
            }
            .authorizeHttpRequests { auth ->
                auth
                    .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/api/movies/**").permitAll()
                    .requestMatchers("/api/theatres/**").permitAll()
                    .requestMatchers("/api/shows/**").permitAll()
                    .requestMatchers("/api/recommend/**").permitAll()
                    .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/reviews/*").permitAll()
                    .requestMatchers("/api/reviews/*/average").permitAll()
                    .requestMatchers("/ws/**").permitAll()
                    .requestMatchers("/index.html").permitAll()
                    .requestMatchers("/*.html").permitAll()
                    .requestMatchers("/*.js").permitAll()
                    .requestMatchers("/*.css").permitAll()
                    .requestMatchers("/").permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}
