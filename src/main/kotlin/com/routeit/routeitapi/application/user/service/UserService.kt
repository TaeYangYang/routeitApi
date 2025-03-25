package com.routeit.routeitapi.application.user.service

import com.routeit.routeitapi.application.token.dto.Token
import com.routeit.routeitapi.application.token.service.TokenService
import com.routeit.routeitapi.application.user.dto.UserDto
import com.routeit.routeitapi.application.user.entity.User
import com.routeit.routeitapi.application.user.repository.UserRepository
import com.routeit.routeitapi.config.jwt.JwtTokenProvider
import com.routeit.routeitapi.exception.BaseRuntimeException
import com.routeit.routeitapi.application.base.mapper.GenericMapper
import com.routeit.routeitapi.application.user.entity.UserRole
import com.routeit.routeitapi.application.user.mapper.UserMapper
import org.springframework.context.support.MessageSourceAccessor
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
  private val bCryptPasswordEncoder: BCryptPasswordEncoder,
  private val messageSourceAccessor: MessageSourceAccessor,
  private val jwtTokenProvider: JwtTokenProvider,
  private val userRepository: UserRepository,
  private val tokenService: TokenService,
  private val userMapper: UserMapper
) {

  /**
   * 회원정보 조회 후 토큰 발급
   *
   * @param userDto
   * @return Token
   */
  fun signIn(userDto: UserDto): Token {
    val user: User = userRepository.findByEmail(userDto.email)
      .orElseThrow { BaseRuntimeException(HttpStatus.BAD_REQUEST, messageSourceAccessor.getMessage("user.login.fail")) }

    // 패스워드 검증
    if(!bCryptPasswordEncoder.matches(userDto.password, user.password)){
      throw BaseRuntimeException()
    }

    // AccessToken 생성
    val claims: HashMap<String, Any> = HashMap();
    claims.set("email", user.email)
    claims.set("role", user.userRole ?: UserRole.USER)
    val accessToken: String = jwtTokenProvider.generateAccessToken(user.email, claims)

    // 기존에 부여된 RefreshToken 제거 후 재발급
    tokenService.removeUserRefreshToken(user.email)
    val refreshToken: String = jwtTokenProvider.generateRefreshToken(user.email)
    tokenService.putRefreshToken(refreshToken, user.email)

    return Token(
      accessToken = "Bearer ${accessToken}",
      refreshToken = "Bearer ${refreshToken}"
    )
  }

  fun findByEmail(email: String): User{
    return userRepository.findByEmail(email)
      .orElseThrow { BaseRuntimeException(HttpStatus.BAD_REQUEST, messageSourceAccessor.getMessage("user.login.fail")) }
  }

  fun signUp(userDto: UserDto): User {
    val user: User = userMapper.toEntity(userDto)
    return userRepository.save(user)
  }

}