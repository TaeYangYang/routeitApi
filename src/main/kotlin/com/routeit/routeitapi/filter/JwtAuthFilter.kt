package com.routeit.routeitapi.filter

import com.routeit.routeitapi.application.token.service.TokenService
import com.routeit.routeitapi.application.user.entity.User
import com.routeit.routeitapi.application.user.entity.UserRole
import com.routeit.routeitapi.application.user.service.UserService
import com.routeit.routeitapi.config.jwt.JwtTokenProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

@Component
class JwtAuthFilter(
  private val jwtTokenProvider: JwtTokenProvider,
  private val userService: UserService,
  private val tokenService: TokenService
) : OncePerRequestFilter(){

  val EXCLUDED_PATHS: Array<String> = arrayOf(
    "/swagger-ui/**", "/api-docs/**", // swagger
    "/api/test/**", // test
    "/api/user/signin", "/api/user/signup" // 로그인, 회원가입
  )

  override fun shouldNotFilter(request: HttpServletRequest): Boolean {
    return (EXCLUDED_PATHS).any { request.requestURI.startsWith(it) }
  }

  /**
   * JWT 처리 필터
   *
   * @param request
   * @param response
   * @param filterChain
   */
  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
    val accessToken: String = request.getHeader("Authrization")?.substring(7) ?: ""
    val refreshToken: String = request.getHeader("Authrization")?.substring(7) ?: ""
    val userId: String = jwtTokenProvider.getUserIdFromToken(accessToken) ?: ""

    // AccessToken 유효한 경우
    if(jwtTokenProvider.validateToken(accessToken)){
      if(SecurityContextHolder.getContext().authentication == null){
        SecurityContextHolder.getContext().authentication = getUserAuth(userId) // SecurityContextHolder 내 인증 정보 없다면 저장
      }
      filterChain.doFilter(request, response)
      return
    } else if(jwtTokenProvider.validateToken(refreshToken)){

    }

    filterChain.doFilter(request, response)
  }

  /**
   * 토큰의 사용자 userId를 얻어 정보 조회 후 UsernamePasswordAuthenticationToken 생성
   *
   * @param userId
   * @return
   */
  fun getUserAuth(userId: String): UsernamePasswordAuthenticationToken{
    val user: User = userService.findByUserId(userId)

    return UsernamePasswordAuthenticationToken(user, user.password, Collections.singleton(SimpleGrantedAuthority(
      user.userRole?.name ?: UserRole.USER.name)))
  }
}