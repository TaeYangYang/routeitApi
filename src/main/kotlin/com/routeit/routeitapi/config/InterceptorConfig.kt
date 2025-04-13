package com.routeit.routeitapi.config

import com.routeit.routeitapi.interceptor.LogInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * 인터셉터 등록
 */
@Configuration
class InterceptorConfig (
  private val logInterceptor: LogInterceptor
): WebMvcConfigurer{
  override fun addInterceptors(registry: InterceptorRegistry) {
    registry.addInterceptor(logInterceptor)
  }
}