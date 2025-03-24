package com.routeit.routeitapi.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.servlet.server.Encoding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.MessageSourceAccessor
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
import java.util.*

@Configuration
class MessageConfig {

  @Bean
  fun defaultLocaleResolver(): LocaleResolver{
    val localeResolver = AcceptHeaderLocaleResolver()
    localeResolver.setDefaultLocale(Locale.KOREAN)
    return localeResolver
  }

  @Bean
  fun messageSource(): ReloadableResourceBundleMessageSource {
    Locale.setDefault(Locale.KOREAN) // default 한국어 처리
    val messageSource = ReloadableResourceBundleMessageSource()
    messageSource.setBasename("classpath:/messages/messages")
    messageSource.setDefaultEncoding(Encoding.DEFAULT_CHARSET.toString())
    messageSource.setDefaultLocale(Locale.getDefault())
    messageSource.setCacheSeconds(600)
    return messageSource
  }

  @Bean
  fun messageSourceAccessor(@Autowired messageSource: ReloadableResourceBundleMessageSource?): MessageSourceAccessor {
    return MessageSourceAccessor(messageSource!!)
  }
}