package com.routeit.routeitapi.application.token.service

import com.routeit.routeitapi.application.token.dto.TokenDto
import com.routeit.routeitapi.application.token.repository.TokenCacheRepository
import com.routeit.routeitapi.application.user.entity.UserRole
import com.routeit.routeitapi.config.jwt.JwtTokenProvider
import com.routeit.routeitapi.config.jwt.TOKEN_PREFIX
import com.routeit.routeitapi.exception.BaseRuntimeException
import com.routeit.routeitapi.exception.InvalidTokenException
import com.routeit.routeitapi.exception.InvalidArgumentsException
import org.springframework.context.support.MessageSourceAccessor
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.*

@Service
class TokenService(
  private val messageSourceAccessor: MessageSourceAccessor,
  private val tokenCacheRepository: TokenCacheRepository,
  private val jwtTokenProvider: JwtTokenProvider
) {

  /**
   * RefreshToken 으로 정보 획득
   *
   * @param refreshToken
   * @return id
   */
  fun getRefreshToken(refreshToken: String): String{
    return Optional.ofNullable(tokenCacheRepository.findByRefreshToken(refreshToken))
      .orElseThrow{BaseRuntimeException(HttpStatus.UNAUTHORIZED, messageSourceAccessor.getMessage("token.null.fail"))}
  }

  /**
   * refresh token 저장
   *
   * @param refreshToken
   * @param userId
   * @param userRole
   */
  fun putRefreshToken(refreshToken: String, userId: String, userRole: UserRole) {
    tokenCacheRepository.save(refreshToken, userId, userRole)
  }

  /**
   * refresh token 삭제
   *
   * @param refreshToken
   */
  private fun removeRefreshToken(refreshToken: String) {
    tokenCacheRepository.deleteByRefreshToken(refreshToken)
  }

  /**
   * id 정보로 refreshToken 조회해서 삭제
   *
   * @param id
   */
  fun removeUserRefreshToken(id: String) {
    tokenCacheRepository.deleteByUserId(id)
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

  /**
   * accessToken, refreshToken 재발급
   *
   * @param tokenDto
   * @return tokenDto
   */
  fun refreshToken(tokenDto: TokenDto): TokenDto{
    val userId = Optional.ofNullable(tokenDto.userId)
      .orElseThrow{InvalidArgumentsException(HttpStatus.UNAUTHORIZED, messageSourceAccessor.getMessage("token.invalid.fail"))}
    var refreshToken = Optional.ofNullable(tokenDto.refreshToken)
      .orElseThrow{InvalidArgumentsException(HttpStatus.UNAUTHORIZED, messageSourceAccessor.getMessage("token.null.fail"))}

    if(refreshToken.startsWith("Bearer")){
      refreshToken = refreshToken.substring(7)
    }

    // 토큰 유효성 검증
    if(!jwtTokenProvider.validateToken(refreshToken!!)){
      throw InvalidTokenException(HttpStatus.UNAUTHORIZED, messageSourceAccessor.getMessage("token.invalid.fail"))
    }

    val roleName = Optional.ofNullable(tokenCacheRepository.findByRefreshTokenAndUserId(refreshToken, userId))
      .orElseThrow{InvalidTokenException(HttpStatus.UNAUTHORIZED, messageSourceAccessor.getMessage("token.invalid.fail"))}

    val userRole = enumValueOf<UserRole>(roleName)

    return generateAllToken(userId, userRole)
  }

  /**
   * accessToken, refreshToken 생성
   *
   * @param userId
   * @param userRole
   * @return
   */
  fun generateAllToken(userId: String, userRole: UserRole): TokenDto{
    // AccessToken 생성
    val claims: HashMap<String, Any> = HashMap();
    claims.set("userId", userId)
    claims.set("role", userRole)
    val accessToken: String = jwtTokenProvider.generateAccessToken(userId, claims)

    // 기존에 부여된 RefreshToken 제거 후 재발급
    this.removeUserRefreshToken(userId)
    val refreshToken: String = jwtTokenProvider.generateRefreshToken(userId)
    this.putRefreshToken(refreshToken, userId, userRole)

    return TokenDto(
      accessToken = "$TOKEN_PREFIX ${accessToken}",
      refreshToken = "$TOKEN_PREFIX ${refreshToken}",
      userId = userId
    )
  }
}