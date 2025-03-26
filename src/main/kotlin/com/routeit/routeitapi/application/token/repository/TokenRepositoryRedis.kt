package com.routeit.routeitapi.application.token.repository

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
class TokenRepositoryRedis(
  private val redisTemplate: RedisTemplate<String, String>
) {
  companion object{
    private const val KEY_PREFIX = "refreshToken" // refreshToken:{userId}:{refreshToken}
  }

  /**
   * RefreshToken을 Redis에 저장
   *
   * @param refreshToken
   * @param userId
   */
  fun save(refreshToken: String, userId: String){
    val key = "$KEY_PREFIX:$userId:$refreshToken"
    redisTemplate.opsForValue().set(key, "", REFRESH_TOKEN_EXP_TIME, TimeUnit.MILLISECONDS)
  }

  /**
   * Redis에서 RefreshToken 조회
   *
   * @param refreshToken
   * @return
   */
  fun findByRefreshToken(refreshToken: String): String?{
    val key = redisTemplate.keys("$KEY_PREFIX:*:$refreshToken").firstOrNull()
    return key?.let{redisTemplate.opsForValue().get(it)}
  }

  /**
   * token값으로 RefreshToken 삭제
   *
   * @param refreshToken
   */
  fun deleteByRefreshToken(refreshToken: String){
    val keys = redisTemplate.keys("$KEY_PREFIX:*:$refreshToken")
    redisTemplate.delete(keys)
  }

  fun deleteByUserId(userId: String){
    val keys = redisTemplate.keys("$KEY_PREFIX:$userId:*")
    redisTemplate.delete(keys)
  }

}