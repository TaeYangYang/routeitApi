package com.routeit.routeitapi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class CorsConfig {

  @Bean
  fun corsConfigurationSource(): CorsConfigurationSource{
    val config = CorsConfiguration()
    config.allowedOrigins = listOf("*")
    config.allowedMethods = listOf("GET", "POST", "DELETE", "PUT")
    config.allowedHeaders = listOf("*")

    val source = UrlBasedCorsConfigurationSource()
    source.registerCorsConfiguration("/**", config)

    return source
  }
}