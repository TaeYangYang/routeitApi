package com.routeit.routeitapi.application.user.repository

import com.routeit.routeitapi.application.base.repository.BaseRepositoryRedis
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryRedis(
  override val redisTemplate: RedisTemplate<String, String>
): BaseRepositoryRedis(redisTemplate) {

}