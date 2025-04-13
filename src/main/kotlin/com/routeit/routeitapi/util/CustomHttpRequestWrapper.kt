package com.routeit.routeitapi.util

import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

/**
 * requestBody를 바이트 배열로 캐싱하여 여러 번 읽을 수 있도록 래핑
 *
 * @param request
 */
class CustomHttpRequestWrapper(request: HttpServletRequest) : HttpServletRequestWrapper(request) {
  val requestBody: ByteArray

  init {
    // Request Body를 바이트 배열로 저장해 여러 번 읽을 수 있도록 캐싱
    val inputStream: InputStream = request.inputStream
    val byteArrayOutputStream: ByteArrayOutputStream = ByteArrayOutputStream()
    val buffer = ByteArray(1024)
    var length: Int
    while ((inputStream.read(buffer).also { length = it }) != -1) {
      byteArrayOutputStream.write(buffer, 0, length)
    }
    this.requestBody = byteArrayOutputStream.toByteArray()
  }

  override fun getInputStream(): ServletInputStream {
    val byteArrayInputStream = ByteArrayInputStream(this.requestBody)
    return object : ServletInputStream() {
      override fun read(): Int {
        return byteArrayInputStream.read()
      }

      override fun isFinished(): Boolean {
        return byteArrayInputStream.available() == 0
      }

      override fun isReady(): Boolean {
        return true
      }

      override fun setReadListener(readListener: ReadListener) {
        // Not implemented
      }
    }
  }
}