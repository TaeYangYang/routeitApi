package com.routeit.routeitapi.application.board.controller

import com.routeit.routeitapi.application.board.dto.BoardPostDto
import com.routeit.routeitapi.application.board.service.BoardPostService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "게시물 API", description = "게시물 데이터 처리 API")
@RestController
@RequestMapping("/api/board/post")
class BoardPostApiController(
  private val boardPostService: BoardPostService
) {

  /**
   * 게시물 작성
   *
   * @param boardDto
   * @return Boolean
   */
  @Operation(summary = "게시물 작성", description = "게시물 작성 API")
  @Parameters(
    value = [
      Parameter(name = "boardDto", description = "")
    ]
  )
  @PostMapping("/")
  fun writePost(@RequestBody boardPostDto: BoardPostDto): Boolean{
    return boardPostService.writePost(boardPostDto)
  }
}