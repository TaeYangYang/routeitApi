package com.routeit.routeitapi.application.board

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "게시물 API", description = "게시물 데이터 처리 API")
@RestController
@RequestMapping("/api/board/post")
class BoardPostApiController {
}