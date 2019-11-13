package com.saxion.workr.general.error

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class ErrorHandler {

    @ExceptionHandler(ErrorResponse::class)
    fun handleErrorResponse(er: ErrorResponse): ResponseEntity<ErrorModel> {
        return ResponseEntity(ErrorModel(er.message, er.httpStatus), er.httpStatus)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleMethodNotSupported(e: HttpRequestMethodNotSupportedException): ResponseEntity<ErrorModel> {
        return ResponseEntity(ErrorModel(e.message, HttpStatus.METHOD_NOT_ALLOWED), HttpStatus.METHOD_NOT_ALLOWED)
    }

    @ExceptionHandler(Exception::class)
    fun handleOtherExceptions(e: Exception): ResponseEntity<ErrorModel> {
        e.printStackTrace()
        return ResponseEntity(ErrorModel(
                "Something went wrong, try again later.",
                HttpStatus.INTERNAL_SERVER_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR)
    }
}