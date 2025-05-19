package com.routeit.routeitapi.interceptor

import com.routeit.routeitapi.application.log.entity.RequestLog
import com.routeit.routeitapi.application.log.repository.RequestLogRepository
import com.routeit.routeitapi.util.CustomHttpRequestWrapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.HandlerMapping
import org.springframework.web.util.ContentCachingResponseWrapper
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ConcurrentHashMap


/**
 * 요청 및 응답 정보 로깅
 */
@Component
class LogInterceptor(
  private val requestLogRepository: RequestLogRepository
): HandlerInterceptor {

  // 요청별 로그 임시 저장소 (응답 후 저장을 위해)
  private val logStorage = ConcurrentHashMap<String, RequestLog>()

  // 전처리
  override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
    val id = UUID.randomUUID().toString()
    val ip = request.remoteAddr ?: "unknown"
    val url = request.requestURL.toString()
    val method = request.method
    val userAgent = request.getHeader("User-Agent") ?: "unknown"

    // 요청별로 본문을 추출해서 저장
    val requestData = mutableMapOf<String, Any>()

    // 각 메소드의 결과가 null이 아닐 경우 map에 추가
    getRequestBody(request)?.let { requestData.putAll(it) }
    getRequestParams(request)?.let { requestData.putAll(it) }
    getPathVariables(handler)?.let { requestData.putAll(it) }

    // 로그 객체 생성
    val log = RequestLog(
      id = id,
      ip = ip,
      url = url,
      method = method,
      timestamp = LocalDateTime.now(),
      userAgent = userAgent,
      requestData = requestData
    )

    // 요청 객체에 logId를 설정하여 응답 후 처리에 사용
    request.setAttribute("logId", id)
    logStorage[id] = log

    return true
  }

  // 후처리
  override fun afterCompletion(
    request: HttpServletRequest,
    response: HttpServletResponse,
    handler: Any,
    ex: Exception?
  ) {
    val logId = request.getAttribute("logId") as? String ?: return
    val log = logStorage.remove(logId) ?: return

    // ContentCachingResponseWrapper로 응답 본문 캐시
    val wrappedResponse = response as ContentCachingResponseWrapper
    val responseBody = wrappedResponse.contentAsByteArray.toString(Charsets.UTF_8)

    // 응답 데이터를 추가하여 로그 생성
    val finalLog = log.copy(responseData = responseBody)

    // MongoDB에 로그 저장
    requestLogRepository.save(finalLog)
  }

  /**
   * requestBody 데이터 추출
   *
   * @param request
   * @return Map
   */
  private fun getRequestBody(request: HttpServletRequest): Map<String, Any>? {
    if (request is CustomHttpRequestWrapper) { // request가 CustomHttpRequestWrapper로 래핑되어 있는지 확인
      val requestBody = String(request.requestBody)
      return mapOf("requestBody" to requestBody)
    } else{
      return null
    }
  }

  /**
   * requestParam 추출
   *
   * @param request
   * @return Map
   */
  private fun getRequestParams(request: HttpServletRequest): Map<String, String> {
    val paramMap: MutableMap<String, String> = HashMap()
    val parameterNames = request.parameterNames

    while (parameterNames.hasMoreElements()) {
      val paramName = parameterNames.nextElement()
      paramMap[paramName] = request.getParameter(paramName)
    }

    return paramMap
  }

  /**
   * pathVariable 추출
   *
   * @param request
   * @return Map
   */
  private fun getPathVariables(handler: Any): Map<String, String>? {
    if(handler is HandlerMethod){
      val attributes: ServletRequestAttributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
      val currentRequest: HttpServletRequest = attributes.request
      val pathVariables = currentRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE) as Map<String, String>?
      return pathVariables
    } else{
      return null
    }
  }
}
