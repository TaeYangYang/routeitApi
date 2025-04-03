package com.routeit.routeitapi.exception

import org.springframework.http.HttpStatus

class InvalidArgumentsException: BaseRuntimeException {

  override var code: Int
  override var status: String
  override var message: String

  constructor(httpStatus: HttpStatus, message: String){
    this.code = httpStatus.value()
    this.status = httpStatus.name
    this.message = message
  }
}