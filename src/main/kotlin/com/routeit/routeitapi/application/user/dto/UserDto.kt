package com.routeit.routeitapi.application.user.dto

import com.routeit.routeitapi.application.user.entity.UserRole

data class UserDto(
  val userId: String? = "",
  val password: String? = "",
  val name: String? = "",
  val nickname: String? = "",
  val ageRange: Int?,
  val gender: String? = "",
  val userRole: UserRole? = UserRole.USER
)