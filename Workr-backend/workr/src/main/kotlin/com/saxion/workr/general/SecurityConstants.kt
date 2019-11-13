package com.saxion.workr.general

//Token setup
object SecurityConstants {
    const val SECRET = "123abc456def789ghi10j123abc456def789ghi10j123abc456def789ghi10j123abc456def789ghi10j123abc456def789ghi10j"
    const val EXPIRATION_TIME: Long = 864000000 // 10 days
    const val TOKEN_PREFIX = "Bearer "
    const val HEADER_STRING = "Authorization"
    const val STRENGTH = 4
}
