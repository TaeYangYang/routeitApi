package com.routeit.routeitapi.config.jwt

import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Function

const val ACCESS_TOKEN_EXP_TIME: Long = 1000 * 60 // 만료시간(ms)
const val REFRESH_TOKEN_EXP_TIME: Long = 1000 * 60 * 60
const val TOKEN_PREFIX: String = "Bearer"
@Component
class JwtTokenProvider {

  val logger = KotlinLogging.logger {}

  @Value("\${jwt.secret}")
  lateinit var secret: String // jwt secret

  private val key by lazy { Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)) } // 암호화 키

  /**
   * token에서 userId 조회
   *
   * @param token
   * @return userId
   */
  fun getUserIdFromToken(token: String): String?{
    return getClaimFromToken(token, Claims::getId)
  }

  /**
   * token에서 claim 조회
   *
   * @param T
   * @param token
   * @param claimsResolVer
   * @return claim
   */
  fun <T> getClaimFromToken(token: String, claimsResolVer: Function<Claims, T>): T? {
    if (!validateToken(token)) {
      return null // 유효성검증
    }
    val claims: Claims = getAllClaimsFromToken(token)
    return claimsResolVer.apply(claims)
  }

  /**
   * 모든 claim 정보 조회
   *
   * @param token
   * @return Claims
   */
  fun getAllClaimsFromToken(token: String): Claims{
    return Jwts.parser()
      .decryptWith(key)
      .build()
      .parseSignedClaims(token)
      .payload
  }

  /**
   * 토큰 만료 일자 조회
   *
   * @param token
   * @return Date
   */
  fun getExpirationDateFromToken(token: String): Date? {
    return getClaimFromToken(token, Claims::getExpiration)
  }

  /**
   * AccessToken 생성
   *
   * @param id
   * @return AccessToken
   */
  fun generateAccessToken(id: String): String{
    return generateAccessToken(id, HashMap())
  }

  /**
   * AccessToken 생성
   *
   * @param id
   * @param claims
   * @return AccessToken
   */
  fun generateAccessToken(id: String, claims: HashMap<String, Any>): String {
    val now = Date()
    val accessExpiration = Date(now.time + ACCESS_TOKEN_EXP_TIME)

    // AccessToken
    return Jwts.builder()
      //.header().add(createHeader()).and()
      .id(id)
      .subject(id) // jwt sub
      .claims(claims) // 권한
      .issuedAt(now) // 발급시간
      .expiration(accessExpiration) // 만료시간
      .signWith(key) // 서명 키 알고리즘
      .compact()
  }

  /**
   * RefreshToken 생성
   *
   * @param id
   * @return RefreshToken
   */
  fun generateRefreshToken(id: String): String{
    val now = Date()
    val refreshExpiration = Date(now.time + REFRESH_TOKEN_EXP_TIME)

    // RefreshToken
    return Jwts.builder()
      .header().add(createHeader()).and()
      .id(id)
      .subject(id) // jwt sub
      .issuedAt(now) // 발급시간
      .expiration(refreshExpiration) // 만료시간
      .signWith(key) // 서명 키 알고리즘
      .compact()
  }

  /**
   * token 유효성 검사
   *
   * @param token
   * @return boolean
   */
  fun validateToken(token: String): Boolean{
    var tokenValue = token
    if(tokenValue.startsWith("$TOKEN_PREFIX ")){
      tokenValue = tokenValue.substring(7)
    }
    try{
      Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(tokenValue)
      return true
    } catch(e: Exception){
      when(e){
        is SecurityException -> logger.warn{"Invalid JWT signature: ${e.message}"}
        is MalformedJwtException -> logger.warn {"Invalid JWT token: ${e.message}"}
        is ExpiredJwtException -> logger.warn{"JWT token is expired: ${e.message}"}
        is UnsupportedJwtException -> logger.warn{"JWT token is unsupported: ${e.message}"}
        is IllegalArgumentException -> logger.warn{"JWT claims string is empty: ${e.message}"}
        else -> logger.warn{"Jwt Valid Exception: ${e.message}"}
      }
      return false
    }
  }

  fun createHeader(): MutableMap<String, Any> {
    val header: MutableMap<String, Any> = HashMap()
    header["typ"] = "JWT"
    //header["alg"] = "HS256"
    return header
  }


}