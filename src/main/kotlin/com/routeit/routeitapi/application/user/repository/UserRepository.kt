package com.routeit.routeitapi.application.user.repository

import com.routeit.routeitapi.application.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, String>{

  /**
   * 유저 정보 조회
   *
   * @param userId
   * @return
   */
  fun findByUserId(userId: String): User?
}