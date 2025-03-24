package com.routeit.routeitapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@EnableCaching
@SpringBootApplication
class RouteitApiApplication

fun main(args: Array<String>) {
  runApplication<RouteitApiApplication>(*args)
}
