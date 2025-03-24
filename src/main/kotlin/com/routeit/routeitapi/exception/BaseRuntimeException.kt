package com.routeit.routeitapi.exception

import org.springframework.http.HttpStatus


class BaseRuntimeException : RuntimeException {
  val code: Int
  val status: String
  override val message: String

  constructor() : super() {
    this.code = HttpStatus.INTERNAL_SERVER_ERROR.value()
    this.status = HttpStatus.INTERNAL_SERVER_ERROR.name
    this.message = "UNDEFINED_INTERNAL_SERVER_ERROR"
  }

  constructor(httpStatus: HttpStatus, message:String) : super(){
    this.code = httpStatus.value()
    this.status = httpStatus.name
    this.message = message
  }

  constructor(code: Int, status: String, message: String) : super(message) {
    this.code = code
    this.status = status
    this.message = message
  }
}

