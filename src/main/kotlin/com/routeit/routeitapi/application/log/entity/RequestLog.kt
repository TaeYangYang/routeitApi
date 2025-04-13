package com.routeit.routeitapi.application.log.entity

import jakarta.persistence.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.*

@Document(collection = "request_log")
data class RequestLog(
  @Id var id: String? = null,
  val ip: String = "",
  val url: String = "",
  val method: String = "",
  val timestamp: LocalDateTime = LocalDateTime.now(),
  val userAgent: String = "",
  val requestData: Map<String, Any>? = null,  // 요청 데이터를 JSON 형식으로 저장
  val responseData: String? = null  // 응답 데이터를 JSON 형식으로 저장
) {
}