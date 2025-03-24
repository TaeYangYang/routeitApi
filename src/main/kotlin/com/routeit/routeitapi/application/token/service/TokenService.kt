package com.routeit.routeitapi.application.token.service

import com.routeit.routeitapi.exception.BaseRuntimeException
import org.springframework.context.support.MessageSourceAccessor
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.*

@Service
class TokenService(
  private val messageSourceAccessor: MessageSourceAccessor
) {

  private val refreshTokenMap: HashMap<String, String> = HashMap()

  /**
   * RefreshToken 으로 정보 획득
   *
   * @param refreshToken
   * @return id
   */
  fun getRefreshToken(refreshToken: String): String{
    return Optional.ofNullable(refreshTokenMap.get(refreshToken))
      .orElseThrow{BaseRuntimeException(HttpStatus.UNAUTHORIZED, messageSourceAccessor.getMessage("token.null.fail"))}
  }

  /**
   * refresh token 저장
   *
   * @param refreshToken
   * @param id
   */
  fun putRefreshToken(refreshToken: String, id: String) {
    refreshTokenMap.put(refreshToken, id)
  }

  /**
   * refresh token 삭제
   *
   * @param refreshToken
   */
  private fun removeRefreshToken(refreshToken: String) {
    refreshTokenMap.remove(refreshToken)
  }

  /**
   * id 정보로 refreshToken 조회해서 삭제
   *
   * @param id
   */
  fun removeUserRefreshToken(id: String) {
    val it: Iterator<Map.Entry<String, String>> =
      refreshTokenMap.entries.iterator()
    while (it.hasNext()) {
      val (key, value) = it.next()
      if (value == id) {
        removeRefreshToken(key)
        break
      }
    }
  }
}