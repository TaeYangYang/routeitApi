package com.routeit.routeitapi.application.user.controller

import com.routeit.routeitapi.application.base.dto.ResponseDto
import com.routeit.routeitapi.application.token.dto.TokenDto
import com.routeit.routeitapi.application.user.dto.UserDto
import com.routeit.routeitapi.application.user.entity.User
import com.routeit.routeitapi.application.user.service.UserService
import com.routeit.routeitapi.exception.BaseRuntimeException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.context.support.MessageSourceAccessor
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@Tag(name = "유저 API", description = "유저 정보 처리 API")
@RestController
@RequestMapping("/api/user")
class UserApiController(
  private val userService: UserService,
  private val messageSourceAccessor: MessageSourceAccessor
) {

  /**
   * 로그인 요청 후 토큰을 반환
   *
   * @param userDto
   * @return
   */
  @Operation(summary = "토큰 획득", description = "로그인 후 TokenDto 객체 반환")
  @Parameters(
    value = [
      Parameter(name = "userDto", description = "userId, password가 포함된 UserDto")
    ]
  )
  @PostMapping("/public/signin")
  fun signin(@RequestBody userDto: UserDto): TokenDto {
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
      Parameter(name = "UserDto", description = "UserDto(userId, password, name, nickname, ageRange, gender, mobileNumber 포함)")]
  )
  @PostMapping("/public/signup")
  fun signup(@RequestBody userDto: UserDto): String {
    return userService.signUp(userDto)
  }

  /**
   * 로그아웃
   *
   * @return
   */
  @Operation(summary = "로그아웃", description = "로그아웃 처리 API")
  @PostMapping("/public/signout")
  fun signout(): String{
    val userId = (SecurityContextHolder.getContext().authentication.principal as User).userId
    return userService.deleteUserRefreshToken(userId!!)
  }

  /**
   * 유저 정보 조회
   * @param userId
   * @return UserDto
   */
  @Operation(summary = "유저 정보 조회", description = "유저 정보 조회 API")
  @Parameters(
    value = [
      Parameter(name = "userId", description = "사용자 아이디")]
  )
  @GetMapping("/{userId}")
  fun getUser(@PathVariable userId: String): UserDto {
    return UserDto("", "","","",0,"")
  }

  /**
   * 아이디 중복검사
   * @param userId
   * @return UserDto
   */
  @Operation(summary = "아이디 중복검사", description = "아이디 중복검사 API")
  @Parameters(
    value = [
      Parameter(name = "userDto", description = "userId 포함된 값", example = "\"userId\": \"user1@example.com\"")]
  )
  @ApiResponses(
    value = [
      ApiResponse(responseCode = "200", description = "true : 중복 사용자 존재, false : 중복 사용자 미존재")
    ]
  )
  @PostMapping("/public/check-duplicate")
  fun checkDuplicate(@RequestBody userDto: UserDto): Boolean {
    val userId = userDto.userId ?: throw BaseRuntimeException(messageSourceAccessor.getMessage("common.error.invalidReq"))
    return userService.checkDuplicate(userId)
  }

  /**
   * 연락처 인증번호 발송
   * @param userDto
   */
  @Operation(summary = "연락처 인증번호 발송", description = "연락처 인증번호 발송 API, 개발환경은 \"000000\" ")
  @Parameters(
    value = [
      Parameter(name = "mobileNumber", description = "userDto", example = "{\"mobileNumber\": \"010-1234-5678\"")]
  )
  @PostMapping("/public/mobile-verification-code")
  fun mobileVerificationCode(@RequestBody userDto: UserDto): Unit{
    val mobileNumber = userDto.mobileNumber ?: throw BaseRuntimeException(messageSourceAccessor.getMessage("common.error.invalidReq"))
    userService.validMobile(mobileNumber)
  }


  /**
   * 연락처 인증번호 일치여부
   *
   * @param userDto
   * @return Boolean
   */
  @Operation(summary = "연락처 인증번호 일치여부", description = "연락처 인증번호 일치여부 검증 API")
  @Parameters(
    value = [
      Parameter(name = "userDto", description = "연락처, 인증번호 포함된 값", example = """
        {
          "mobleNumber":"010-1234-5678",
           "verificationCode":"123456" 
        }
      """)
    ]
  )
  @ApiResponses(
    value = [
      ApiResponse(responseCode = "200", description = "true : 일치, false : 불일치")
    ]
  )
  @PostMapping("/public/check-verification-code")
  fun checkVerificationCode(@RequestBody userDto: UserDto): Boolean {
    return userService.checkVerificationCode(userDto)
  }

  /**
   * 아이디 찾기
   *
   * @param userDto
   * @return
   */
  @Operation(summary = "아이디 찾기", description = "아이디 찾기 API")
  @Parameters(
    value = [
      Parameter(name = "userDto", description = "name, mobileNumber 포함된 json", example = """
        {
          "name": "홍길동",
          "mobileNumber":"010-1234-5678"
        }
      """)
    ]
  )
  @PostMapping("/public/findUserId")
  fun findUserId(@RequestBody userDto: UserDto): String?{
    return userService.findUserId(userDto)
  }

  /**
   * 패스워드 변경
   *
   * @param userDto
   * @return
   */
  @Operation(summary = "패스워드 변경", description = "패스워드 변경 API")
  @Parameters(
    value = [
      Parameter(name = "userDto", description = "userId, password", example = """
        {
          "userId": "user1@example.com",
          "password":"user1!"
        }
      """)
    ]
  )
  @ApiResponse(responseCode = "200", description = "true")
  @PatchMapping("/password")
  fun updatePassword(@RequestBody userDto: UserDto): Boolean {
    return userService.updatePassword(userDto)
  }

}