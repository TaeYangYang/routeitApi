package com.routeit.routeitapi.application.user.controller

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@WebMvcTest(UserApiController::class)
class UserApiControllerTest {

  @Autowired
  private lateinit var mockMvc: MockMvc

  @Test
  fun test() {
    mockMvc.post("/api/user/signin")
  }
}