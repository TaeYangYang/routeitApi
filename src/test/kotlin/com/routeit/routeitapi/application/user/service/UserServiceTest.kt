package com.routeit.routeitapi.application.user.service

import com.routeit.routeitapi.application.user.dto.UserDto
import com.routeit.routeitapi.application.user.entity.User
import com.routeit.routeitapi.application.user.entity.UserRole
import com.routeit.routeitapi.application.user.mapper.UserMapper
import com.routeit.routeitapi.application.user.repository.UserRepository
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootTest
class UserServiceTest @Autowired constructor(
  private val bCryptPasswordEncoder: BCryptPasswordEncoder,
  private val userMapper: UserMapper,
  private val userRepository: UserRepository
) {

  @Test
  @DisplayName(value = "회원가입 테스트")
  fun signUp() {
    val userDto:UserDto = UserDto(
    email = "user2@example.com",
    password = bCryptPasswordEncoder.encode("user2!"),
    name = "사용자2",
    nickname = "닉네임2",
    ageRange = 20,
    gender = "M",
    userRole = UserRole.USER
    )

    val user: User = userMapper.toEntity(userDto)
    userRepository.save(user)
  }
}