package com.routeit.routeitapi.application.user.repository

import com.routeit.routeitapi.application.base.repository.BaseCacheRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class UserCacheRepository(
  override val redisTemplate: RedisTemplate<String, String>
): BaseCacheRepository(redisTemplate) {

}