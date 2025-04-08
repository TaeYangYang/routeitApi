package com.routeit.routeitapi.application.token

import com.routeit.routeitapi.application.token.dto.TokenDto
import com.routeit.routeitapi.application.token.service.TokenService
import com.routeit.routeitapi.exception.BaseRuntimeException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import org.springframework.context.support.MessageSourceAccessor
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/token")
class TokenApiController(
  private val tokenService: TokenService,
  private val messageSourceAccessor: MessageSourceAccessor
) {

  /**
   * 토큰 유효성 검증
   *
   * @param body
   * @return Boolean
   */
  @Operation(summary = "토큰 유효성 검증", description = "토큰(AccessToken or RefreshToken) 검증 API")
  @Parameters(
    value = [
      Parameter(name = "token", description = "token값")]
  )
  @PostMapping("/public/valid")
  fun validToken(@RequestBody body: Map<String, String>): Boolean {
    val token = body["token"] ?: throw BaseRuntimeException(messageSourceAccessor.getMessage("token.null.fail"))
    return tokenService.validToken(token)
  }

  /**
   * 토큰 재발급
   *
   * @param body
   * @return Boolean
   */
  @Operation(summary = "토큰 재발급", description = "토큰 재발급 API")
  @Parameters(
    value = [
      Parameter(name = "tokenDto", description = "accessToken, refreshToken, userId")]
  )
  @PostMapping("/public/refresh")
  fun refresh(@RequestBody tokenDto: TokenDto): TokenDto {
    return tokenService.refreshToken(tokenDto)
  }

}