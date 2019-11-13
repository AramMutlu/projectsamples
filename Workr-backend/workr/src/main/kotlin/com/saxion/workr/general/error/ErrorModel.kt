package com.saxion.workr.general.error

import org.springframework.http.HttpStatus
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ErrorModel(val message: String?,
                      val httpStatus: HttpStatus,
                      val timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))