package com.routeit.routeitapi.config.handler

import com.routeit.routeitapi.application.base.dto.ResponseDto
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerExceptionResolver

/**
 * 인증되지 않거나 유효하지 않은 인증 정보를 가지고 있는 경우 처리
 */
@Component
class CustomAuthenticationEntryPointHandler(
  @Qualifier("handlerExceptionResolver") private val handlerExceptionResolver: HandlerExceptionResolver
): AuthenticationEntryPoint {


  override fun commence(
    request: HttpServletRequest?,
    response: HttpServletResponse?,
    authException: AuthenticationException?
  ) {
    if(response == null){
      return
    }

    response.characterEncoding = "UTF-8"
    response.contentType = MediaType.APPLICATION_JSON_VALUE
    response.status = HttpStatus.UNAUTHORIZED.value()
//
//    val responseJson: JSONObject = JSONObject()
//    responseJson.put("message", exceptionCode.getMessage())
//    responseJson.put("code", exceptionCode.getCode())
//
//    response.writer.print(responseJson)
//
//    return objectMapper.writeValueAsString(ResponseDto(HttpStatus.OK.value(), "SUCCESS", HttpStatus.OK.reasonPhrase, body))



  }
}