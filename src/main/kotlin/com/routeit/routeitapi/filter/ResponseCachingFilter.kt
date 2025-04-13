package com.routeit.routeitapi.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingResponseWrapper

/**
 * 응답 내용 저장해두고 인터셉터에서 응답값 받을 수 있도록 처리
 */
@Component
class ResponseCachingFilter : OncePerRequestFilter() {
  override fun doFilterInternal(
    request: HttpServletRequest,
    response: HttpServletResponse,
    filterChain: FilterChain
  ) {
    val wrappedResponse = ContentCachingResponseWrapper(response)

    // 체인 실행 (응답이 이 시점에 처리됨)
    filterChain.doFilter(request, wrappedResponse)

    // 응답 본문 복사 후 다시 클라이언트에게 전달
    wrappedResponse.copyBodyToResponse()
  }
}