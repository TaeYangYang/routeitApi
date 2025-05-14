package com.routeit.routeitapi.application.board.service

import com.routeit.routeitapi.application.board.dto.BoardPostDto
import com.routeit.routeitapi.application.board.entity.BoardPost
import com.routeit.routeitapi.application.board.mapper.BoardPostMapper
import com.routeit.routeitapi.application.board.repository.BoardPostRepository
import org.springframework.stereotype.Service

@Service
class BoardPostService(
  private val boardPostRepository: BoardPostRepository,
  private val boardPostMapper: BoardPostMapper
) {

  fun writePost(boardPostDto: BoardPostDto): Boolean{
    val boardPost: BoardPost = boardPostMapper.toEntity(boardPostDto)
    boardPostRepository.save(boardPost)
    return true
  }
}