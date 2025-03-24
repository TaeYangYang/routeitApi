package com.routeit.routeitapi.application.base.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.routeit.routeitapi.application.base.dto.ResponseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@RestControllerAdvice(annotations = [RestController::class], basePackages = ["com.routeit.routeitapi.application.*.controller"])
class BaseControllerAdvice @Autowired constructor(private val objectMapper: ObjectMapper): ResponseBodyAdvice<Any>{

  // 현재 Controller작업이 끝난 response를 beforeBodyWrite로 보낼 것인지
  override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean = true

  override fun beforeBodyWrite(
    body: Any?,
    returnType: MethodParameter,
    selectedContentType: MediaType,
    selectedConverterType: Class<out HttpMessageConverter<*>>,
    request: ServerHttpRequest,
    response: ServerHttpResponse
  ): Any? {
    // swagger 요청 필터링
    val path: String = request.uri.path
    if(path.startsWith("/swagger-ui") || path.startsWith("/api-docs")){
      return body
    }

    when (body) {
      null -> return null
      is String -> {
        // String을 JSON 문자열로 변환 (StringHttpMessageConverter 우회)
        response.headers.contentType = MediaType.APPLICATION_JSON
        return objectMapper.writeValueAsString(ResponseDto(HttpStatus.OK.value(), "SUCCESS", HttpStatus.OK.reasonPhrase, body))
      }
      else -> return ResponseDto(HttpStatus.OK.value(), "SUCCESS", HttpStatus.OK.reasonPhrase, body)
    }
  }
}