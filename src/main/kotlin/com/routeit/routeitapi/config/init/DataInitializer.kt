package com.routeit.routeitapi.config.init

import com.routeit.routeitapi.application.user.entity.User
import com.routeit.routeitapi.application.user.entity.UserRole
import com.routeit.routeitapi.application.user.repository.UserRepository
import com.routeit.routeitapi.application.user.service.UserService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class DataInitializer(
  private val bCryptPasswordEncoder: BCryptPasswordEncoder,
  private val userRepository: UserRepository
): ApplicationRunner {

  override fun run(args: ApplicationArguments?) {
    var user:User = User(
      email = "user1@example.com",
      password = bCryptPasswordEncoder.encode("user1!"),
      name = "사용자1",
      nickname = "닉네임1",
      ageRange = 20,
      gender = "M",
      userRole = UserRole.USER
    )

    userRepository.save(user)
  }

}