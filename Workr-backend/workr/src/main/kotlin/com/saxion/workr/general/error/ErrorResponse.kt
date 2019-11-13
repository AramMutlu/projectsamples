package com.saxion.workr.general.error

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest



//Return error

class ErrorResponse(override val message: String? = null, val httpStatus: HttpStatus) : RuntimeException()