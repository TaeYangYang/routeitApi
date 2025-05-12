package com.routeit.routeitapi.application.board.repository

import com.routeit.routeitapi.application.board.entity.BoardPost
import org.springframework.data.jpa.repository.JpaRepository

interface BoardPostRepository: JpaRepository<BoardPost, Long> {

}