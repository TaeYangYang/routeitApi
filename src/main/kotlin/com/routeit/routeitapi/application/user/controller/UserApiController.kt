package com.routeit.routeitapi.application.user.controller

import com.routeit.routeitapi.application.token.dto.Token
import com.routeit.routeitapi.application.user.dto.UserDto
import com.routeit.routeitapi.application.user.entity.User
import com.routeit.routeitapi.application.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "유저 API", description = "유저 정보 처리 API")
@RestController
@RequestMapping("/api/user")
class UserApiController(
  private val userService: UserService
) {

  /**
   * 로그인 요청 후 토큰을 반환
   *
   * @param userDto
   * @return
   */
  @Operation(summary = "토큰 획득", description = "로그인 후 AccessToken, RefreshToken DTO 객체 반환")
  @Parameters(
    value = [
      Parameter(name = "HttpServletRequest", description = "요청", example = "user1@example.com"),
      Parameter(name = "userDto", description = "userId, password가 포함된 유저 정보", example = "user1!")
    ]
  )
  @PostMapping("/signin")
  fun signin(@RequestBody userDto: UserDto): Token {
    return userService.signIn(userDto)
  }

  /**
   * 회원가입
   *
   * @param userDto
   * @return
   */
  @Operation(summary = "회원가입", description = "회원가입 처리 API")
  @Parameters(
    value = [
      Parameter(name = "UserDto", description = "UserDto(userId, password, name, nickname, ageRange, gender 포함)")]
  )
  @PostMapping("/signup")
  fun signup(@RequestBody userDto: UserDto): User {
    return userService.signUp(userDto)
  }

}