package com.routeit.routeitapi.config

import com.routeit.routeitapi.filter.JwtAuthFilter
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
  private val jwtAuthFilter: JwtAuthFilter
){
  val EXCLUDED_PATHS: Array<String> = arrayOf(
    "/swagger-ui/**", "/api-docs/**", // swagger
    "/api/test/**", // test
    "/api/user/signin", "/api/user/signup" // 로그인, 회원가입
  )


  @Bean
  fun filterChain(http: HttpSecurity): SecurityFilterChain{
    http
      .csrf { it.disable() } // cmt: h2로그인은 csrf처리 안되어있고 jwt 인증은 서버에 인증정보를 저장하지 않으므로 disabled
      .headers { it.frameOptions { it.disable() } } // Clickjacking 공격 방지, frame이나 iframe 안됨
      .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) } // JWT 사용을 위한 세션 미사용 처리
      .authorizeHttpRequests {
        it.requestMatchers(*EXCLUDED_PATHS).permitAll() // 체크 제외
          .requestMatchers(PathRequest.toH2Console()).permitAll() // H2 콘솔 접근 허용
          .anyRequest().authenticated() // 이 외 모두 인증 필요
      }

      .formLogin { it.disable() } // 기본 formlogi 대신 jwt를 사용한 커스텀 기능 사용
      .logout { it.disable() }

    http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

    return http.build()
  }
}