package com.routeit.routeitapi.application.token

import com.routeit.routeitapi.application.token.service.TokenService
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
  @PostMapping("/valid")
  fun validToken(@RequestBody body: Map<String, String>): Boolean {
    val token = body["token"] ?: throw IllegalArgumentException(messageSourceAccessor.getMessage("token.null.fail"))
    return tokenService.validToken(token)
  }

}