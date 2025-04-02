package com.routeit.routeitapi.application.token.service

import com.routeit.routeitapi.application.token.repository.TokenRepositoryRedis
import com.routeit.routeitapi.config.jwt.JwtTokenProvider
import com.routeit.routeitapi.exception.BaseRuntimeException
import org.springframework.context.support.MessageSourceAccessor
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.*

@Service
class TokenService(
  private val messageSourceAccessor: MessageSourceAccessor,
  private val tokenRepositoryRedis: TokenRepositoryRedis,
  private val jwtTokenProvider: JwtTokenProvider
) {

  /**
   * RefreshToken 으로 정보 획득
   *
   * @param refreshToken
   * @return id
   */
  fun getRefreshToken(refreshToken: String): String{
    return Optional.ofNullable(tokenRepositoryRedis.findByRefreshToken(refreshToken))
      .orElseThrow{BaseRuntimeException(HttpStatus.UNAUTHORIZED, messageSourceAccessor.getMessage("token.null.fail"))}
  }

  /**
   * refresh token 저장
   *
   * @param refreshToken
   * @param id
   */
  fun putRefreshToken(refreshToken: String, userId: String) {
    tokenRepositoryRedis.save(refreshToken, userId)
  }

  /**
   * refresh token 삭제
   *
   * @param refreshToken
   */
  private fun removeRefreshToken(refreshToken: String) {
    tokenRepositoryRedis.deleteByRefreshToken(refreshToken)
  }

  /**
   * id 정보로 refreshToken 조회해서 삭제
   *
   * @param id
   */
  fun removeUserRefreshToken(id: String) {
    tokenRepositoryRedis.deleteByUserId(id)
  }

  /**
   * 토큰 유효성 검증
   *
   * @param token
   * @return valid or invalid
   */
  fun validToken(token: String): Boolean{
    return jwtTokenProvider.validateToken(token)
  }
}