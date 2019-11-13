package com.saxion.workr.general

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.saxion.workr.general.SecurityConstants.EXPIRATION_TIME
import com.saxion.workr.general.SecurityConstants.HEADER_STRING
import com.saxion.workr.general.SecurityConstants.SECRET
import com.saxion.workr.general.SecurityConstants.TOKEN_PREFIX
import com.saxion.workr.general.error.ErrorResponse
import com.saxion.workr.models.Login
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

import java.io.IOException
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

//Checks authentication
class JWTAuthenticationFilter(private val _authenticationManager: AuthenticationManager) :
        UsernamePasswordAuthenticationFilter() {

    //The user attempted to login
    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(
            req: HttpServletRequest,
            res: HttpServletResponse?
    ): Authentication {
        return try {
            val creds = ObjectMapper().enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                    .readValue(req.inputStream, Login::class.java)

            _authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(
                            creds.username,
                            creds.password,
                            ArrayList<GrantedAuthority>()
                    )
            )
        } catch (e: IOException) {
            throw AuthenticationServiceException(e.message)
        }
    }

    //Login is invalid
    @Throws(IOException::class, ServletException::class)
    override fun unsuccessfulAuthentication(request: HttpServletRequest?, response: HttpServletResponse?, failed: AuthenticationException?) {
        throw ErrorResponse("Invalid username and/or password", HttpStatus.UNAUTHORIZED)
    }

    //User/password is correct. Generate token and return in header
    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(
            req: HttpServletRequest,
            res: HttpServletResponse,
            chain: FilterChain?,
            auth: Authentication
    ) {
        val claims: MutableList<String> = mutableListOf()
        auth.authorities!!.forEach { a -> claims.add(a.toString()) }

        val token = Jwts.builder()
                .setSubject((auth.principal as User).username)
                .claim("auth", claims)
                .setExpiration(Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(SECRET.toByteArray()), SignatureAlgorithm.HS512)
                .compact()
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token)

        res.addHeader("access-control-expose-headers", HEADER_STRING);
    }
}
