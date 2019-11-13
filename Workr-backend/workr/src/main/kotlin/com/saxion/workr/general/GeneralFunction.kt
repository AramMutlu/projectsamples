package com.saxion.workr.general

import com.saxion.workr.general.error.ErrorResponse
import com.saxion.workr.models.User
import com.saxion.workr.repositories.UserRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

//Checks the bearer and returns an user
//Used because we use it allot
class GeneralFunction(@Autowired val userRepository: UserRepository) {
    fun checkBearer(authorization: String): User {
        val bearer = authorization.replace("Bearer ", "")
        val parsedToken: Jws<Claims>
        try {
            val signingKey = SecurityConstants.SECRET.toByteArray();

            parsedToken = Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(bearer);

            return userRepository.getByUsername(parsedToken.body["sub"].toString()) //username
                    ?: throw ErrorResponse("User not found", HttpStatus.NOT_FOUND)
        } catch (_: Exception) {
            throw ErrorResponse("You are not logged in.", HttpStatus.FORBIDDEN)
        }
    }
}