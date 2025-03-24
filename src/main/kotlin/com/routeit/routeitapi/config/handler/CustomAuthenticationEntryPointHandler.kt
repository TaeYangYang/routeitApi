package com.routeit.routeitapi.config.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

/**
 * 인증되지 않거나 유효하지 않은 인증 정보를 가지고 있는 경우 처리
 */
class CustomAuthenticationEntryPointHandler(): AuthenticationEntryPoint {

  override fun commence(
    request: HttpServletRequest?,
    response: HttpServletResponse?,
    authException: AuthenticationException?
  ) {
    if(response == null){
      return
    }

    response.status = HttpStatus.UNAUTHORIZED.value()
    response.characterEncoding = "UTF-8"
    response.contentType = "application/json"
  }
}