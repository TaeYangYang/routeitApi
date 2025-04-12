package com.routeit.routeitapi.application.token.repository

import com.routeit.routeitapi.application.base.repository.BaseCacheRepository
import com.routeit.routeitapi.application.user.entity.UserRole
import com.routeit.routeitapi.config.jwt.REFRESH_TOKEN_EXP_TIME
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

/**
 * Token 관련 CRUD
 *
 * @property redisTemplate
 */
@Repository
class TokenCacheRepository(
  override val redisTemplate: RedisTemplate<String, String>
): BaseCacheRepository(redisTemplate) {

  companion object{
    private const val KEY_PREFIX = "refreshToken" // refreshToken:{userId}:{refreshToken}
  }

  /**
   * RefreshToken을 Redis에 저장
   *
   * @param refreshToken
   * @param userId
   */
  fun save(refreshToken: String, userId: String, userRole: UserRole){
    val key = "$KEY_PREFIX:$userId:$refreshToken"
    super.save(key, userRole.name, REFRESH_TOKEN_EXP_TIME, TimeUnit.MILLISECONDS)
  }

  /**
   * Redis에서 RefreshToken 조회
   *
   * @param refreshToken
   * @return
   */
  fun findByRefreshToken(refreshToken: String): String?{
    return super.findByKey("$KEY_PREFIX:*:$refreshToken")
  }


  /**
   * Redis에서 RefreshToken 조회
   *
   * @param refreshToken
   * @param userId
   * @return
   */
  fun findByRefreshTokenAndUserId(refreshToken: String, userId: String): String?{
    return super.findByKey("$KEY_PREFIX:$userId:$refreshToken")
  }

  /**
   * token값으로 RefreshToken 삭제
   *
   * @param refreshToken
   */
  fun deleteByRefreshToken(refreshToken: String){
    super.deleteByKey("$KEY_PREFIX:*:$refreshToken")
  }

  fun deleteByUserId(userId: String){
    super.deleteByKey("$KEY_PREFIX:$userId:*")
  }

}