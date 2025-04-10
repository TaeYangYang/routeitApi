package com.routeit.routeitapi.application.user.entity

import com.fasterxml.jackson.databind.ser.Serializers.Base
import com.routeit.routeitapi.application.base.entity.BaseEntity
import com.routeit.routeitapi.application.user.dto.UserDto
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.hibernate.annotations.DynamicUpdate

@Entity
@Table(name = "TB_USER")
@DynamicUpdate
@Schema(description = "유저")
class User(
  userId: String?,
  password: String?,
  name: String?,
  nickname: String?,
  ageRange: Int?,
  gender: String?,
  userRole: UserRole? = UserRole.USER,
  mobileNumber: String?
): BaseEntity() {
  @Id
  @Column(length = 100)
  @Comment("이메일")
  @Schema(description = "이메일", example = "user1@example.com")
  var userId: String? = userId
    protected set

  @Column(length = 100)
  @Comment("비밀번호")
  @Schema(description = "비밀번호")
  var password: String? = password
    protected set

  @Column(length = 100)
  @Comment("이름")
  @Schema(description = "이름", example = "홍길동")
  var name: String? = name
    protected set


  @Column(length = 20)
  @Comment("닉네임")
  @Schema(description = "닉네임", example = "루트잇")
  var nickname: String? = nickname
    protected set

  @Comment("연령대")
  @Schema(description = "연령대", example = "20")
  var ageRange: Int? = ageRange
    protected set

  @Column(length = 1)
  @Comment("성별")
  @Schema(description = "성별(M, F)", example = "M, F")
  var gender: String? = gender
    protected set

  @Column(length = 20)
  @Enumerated(EnumType.STRING)
  @Comment("권한")
  var userRole: UserRole? = userRole
    protected set

  @Column(length = 20)
  @Comment("휴대전화")
  @Schema(description = "휴대전화", example = "010-1234-5678")
  var mobileNumber: String? = mobileNumber
    protected set


  fun updateUser(updateUser: UserDto){
    updateUser.password?.let { this.password = it }
  }
}