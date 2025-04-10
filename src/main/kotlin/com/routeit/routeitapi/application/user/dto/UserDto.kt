package com.routeit.routeitapi.application.user.dto

import com.routeit.routeitapi.application.user.entity.UserRole

data class UserDto(
  var userId: String? = "",
  var password: String? = "",
  var name: String? = "",
  var nickname: String? = "",
  var ageRange: Int? = null,
  var gender: String? = "",
  var userRole: UserRole? = UserRole.USER,
  var mobileNumber: String? = "",
  var verificationCode: String? = ""
)