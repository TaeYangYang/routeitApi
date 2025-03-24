package com.routeit.routeitapi.application.test

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "테스트 API", description = "테스트용 API")
@RestController
@RequestMapping("/api/test")
class TestApiController {

  @Operation(summary = "문자열 응답", description = "문자열 응답해주는 API")
  @GetMapping("/index")
  fun index(): String{
    return "index"
  }

  @Operation(summary = "요청 문자열 반복 응답", description = "요청 문자열을 2번 응답해주는 API")
  @Parameter(name = "str", description = "2번 반복되는 문자열")
  @GetMapping("/repeat/{str}")
  fun repeat(@PathVariable("str") str: String): String{
    return str.repeat(2)
  }
}