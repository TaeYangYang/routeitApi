package com.routeit.routeitapi.application.user.service

import com.routeit.routeitapi.application.token.dto.TokenDto
import com.routeit.routeitapi.application.token.service.TokenService
import com.routeit.routeitapi.application.user.dto.UserDto
import com.routeit.routeitapi.application.user.entity.User
import com.routeit.routeitapi.application.user.repository.UserRepository
import com.routeit.routeitapi.config.jwt.JwtTokenProvider
import com.routeit.routeitapi.exception.BaseRuntimeException
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
  fun signIn(userDto: UserDto): TokenDto {
    val user: User = userRepository.findByUserId(userDto.userId!!) ?: throw BaseRuntimeException(HttpStatus.BAD_REQUEST, messageSourceAccessor.getMessage("user.login.fail"))

    // 패스워드 검증
    if(!bCryptPasswordEncoder.matches(userDto.password, user.password)){
      throw BaseRuntimeException()
    }

    return tokenService.generateAllToken(user.userId!!, user.userRole!!)
  }

  fun findByUserId(userId: String): User{
    return userRepository.findByUserId(userId) ?: throw BaseRuntimeException(HttpStatus.BAD_REQUEST, messageSourceAccessor.getMessage("user.login.fail"))
  }

  fun signUp(userDto: UserDto): String {
    val user: User = userMapper.toEntity(userDto)
    userRepository.save(user)
    return "success"
  }

  fun deleteUserRefreshToken(userId: String): String {
    tokenService.removeUserRefreshToken(userId)
    return messageSourceAccessor.getMessage("user.logout.success")
  }

}