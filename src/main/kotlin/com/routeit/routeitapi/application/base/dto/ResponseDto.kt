package com.routeit.routeitapi.application.base.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus


@Schema(description = "응답 객체")
data class ResponseDto<T> (
  @Schema(description = "응답코드", example = "200")
  val code: Int,

  @Schema(description = "상태값", example = "SUCCESS")
  val status: String,

  @Schema(description = "메세지", example = "잘못된 요청입니다.")
  val message: String,

  @Schema(description = "객체", example = "Object")
  val data: T?
)

