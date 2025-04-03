package com.routeit.routeitapi.application.token.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "JWT 토큰")
data class TokenDto(

  @Schema(description = "ACCESS TOKEN")
  val accessToken: String? = "",

  @Schema(description = "REFRESH TOKEN")
  val refreshToken: String? = "",

  @Schema(description = "사용자 ID")
  val userId: String? = ""
) {
  constructor(refreshToken: String, userId: String) : this("", refreshToken, userId)
}
