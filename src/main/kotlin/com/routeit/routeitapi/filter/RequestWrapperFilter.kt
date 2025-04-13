package com.routeit.routeitapi.filter

import com.routeit.routeitapi.util.CustomHttpRequestWrapper
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest

/**
 * 모든 요청을 CustomHttpRequestWrapper로 감싸주는 필터
 */
class RequestWrapperFilter: Filter {

  override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
    if(request is HttpServletRequest){
      val requestWrapper = CustomHttpRequestWrapper(request)
      chain?.doFilter(requestWrapper, response)
    } else{
      chain?.doFilter(request, response)
    }
  }
}