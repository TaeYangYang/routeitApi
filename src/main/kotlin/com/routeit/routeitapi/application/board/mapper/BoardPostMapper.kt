package com.routeit.routeitapi.application.board.mapper

import com.routeit.routeitapi.application.base.mapper.GenericMapper
import com.routeit.routeitapi.application.board.dto.BoardPostDto
import com.routeit.routeitapi.application.board.entity.BoardPost
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface BoardPostMapper: GenericMapper<BoardPostDto, BoardPost> {
}