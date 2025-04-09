package com.routeit.routeitapi.application.base.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class BaseRepositoryRedis(
  protected val redisTemplate: RedisTemplate<String, String>
) {

  /**
   * key, value 받아 redis 저장
   *
   * @param key
   * @param value
   */
  fun save(key: String, value: String, timeout: Long? = null, unit: TimeUnit? = null) {
    if(timeout != null && unit != null){
      redisTemplate.opsForValue().set(key, value, timeout, unit)
    } else{
      redisTemplate.opsForValue().set(key, value)
    }
  }

  /**
   * key 값으로 value 조회
   *
   * @param key
   * @return
   */
  fun findByKey(key: String): String? {
    return redisTemplate.keys(key).firstOrNull()?.let{redisTemplate.opsForValue().get(it)}
  }

  /**
   * key 값으로 데이터 삭제
   *
   * @param key
   */
  fun deleteByKey(key: String) {
    redisTemplate.delete(redisTemplate.keys(key))
  }
}