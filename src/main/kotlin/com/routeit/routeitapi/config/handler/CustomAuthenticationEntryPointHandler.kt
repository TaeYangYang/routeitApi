package com.routeit.routeitapi.config.handler

import com.routeit.routeitapi.exception.InvalidTokenException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.json.simple.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

/**
 * 인증되지 않거나 유효하지 않은 인증 정보를 가지고 있는 경우 처리
 */
@Component
class CustomAuthenticationEntryPointHandler: AuthenticationEntryPoint {


  override fun commence(
    request: HttpServletRequest?,
    response: HttpServletResponse?,
    authException: AuthenticationException?
  ) {
    if(response == null){
      return
    }

    val e = request?.getAttribute("exception") as InvalidTokenException

    response.characterEncoding = "UTF-8"
    response.contentType = MediaType.APPLICATION_JSON_VALUE
    response.status = HttpStatus.UNAUTHORIZED.value()

    val responseJson: JSONObject = JSONObject()
    responseJson.put("code", e.code)
    responseJson.put("status", e.status)
    responseJson.put("message", e.message)

    response.writer.print(responseJson)
  }
}