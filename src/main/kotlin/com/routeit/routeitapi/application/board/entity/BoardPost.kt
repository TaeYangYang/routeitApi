package com.routeit.routeitapi.application.board.entity

import com.routeit.routeitapi.application.base.entity.BaseEntity
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.hibernate.annotations.DynamicUpdate

@Entity
@Table(name = "TB_BOARD_POST")
@DynamicUpdate
@Schema(description = "게시물")
class BoardPost(
  boardPostId: String,
  title: String,
  content: String?
): BaseEntity() {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Comment("게시판 ID")
  @Schema(description = "게시판 ID")
  var boardPostId: String = boardPostId
  protected set

  @Column(length = 100)
  @Comment("제목")
  @Schema(description = "제목")
  var title: String = title
  protected set

  @Lob
  @Comment("내용")
  @Schema(description = "내용")
  var content: String? = content
  protected set

}