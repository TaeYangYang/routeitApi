package com.routeit.routeitapi.application.user.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.routeit.routeitapi.application.base.controller.BaseControllerAdvice
import com.routeit.routeitapi.application.user.service.UserService
import com.routeit.routeitapi.config.jwt.JwtTokenProvider
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.support.MessageSourceAccessor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers

@WebMvcTest(
  controllers = [UserApiController::class],
  excludeAutoConfiguration = [SecurityAutoConfiguration::class, SecurityFilterAutoConfiguration::class]
)
@Import(UserApiControllerTest.ManualMockConfig::class)
class UserApiControllerTest {

  @Autowired
  private lateinit var mockMvc: MockMvc

  @Test
  fun test() {
    mockMvc.post("/api/user/signin") {
      contentType = org.springframework.http.MediaType.APPLICATION_JSON
      content = """{"userId":"user1@email.com", "password":"1234"}"""
    }.andDo {
      print()
    }.andExpect {
      status { isOk() }
    }
  }

  @Test
  fun checkDuplicate() {
    mockMvc.get("/api/user/public/check-duplicate") {
      param("userId", "user1@example.com")
    }.andDo {
      print()
    }.andExpect {
      status { isOk() }
    }
  }

  @TestConfiguration
  class ManualMockConfig {

    @Bean
    fun userService(): UserService = mock(UserService::class.java)

    @Bean
    fun messageSourceAccessor(): MessageSourceAccessor = mock(MessageSourceAccessor::class.java)

    @Bean
    fun baseControllerAdvice(): BaseControllerAdvice =
      BaseControllerAdvice(ObjectMapper(), messageSourceAccessor())

    @Bean
    fun jwtTokenProvider(): JwtTokenProvider = mock(JwtTokenProvider::class.java)
  }
}
