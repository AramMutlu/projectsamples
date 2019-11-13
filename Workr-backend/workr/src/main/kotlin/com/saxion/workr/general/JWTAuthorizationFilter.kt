package com.saxion.workr.general

import com.saxion.workr.general.SecurityConstants.HEADER_STRING
import com.saxion.workr.general.SecurityConstants.SECRET
import com.saxion.workr.general.SecurityConstants.TOKEN_PREFIX
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

import java.io.IOException
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

//Checks authorization token
class JWTAuthorizationFilter(authManager: AuthenticationManager) : BasicAuthenticationFilter(authManager) {


    //Check authorization tokens that were generated for users
    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
            req: HttpServletRequest,
            res: HttpServletResponse,
            chain: FilterChain
    ) {
        val header = req.getHeader(HEADER_STRING)
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res)
            return
        }
        val authentication = getAuthentication(header)
        if (authentication != null) {
            SecurityContextHolder.getContext().authentication = authentication
        }
        chain.doFilter(req, res)
    }

    //When a request required authorization, this checks if its valid
    private fun getAuthentication(token: String): UsernamePasswordAuthenticationToken? {
        return try {
            val claims = Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET.toByteArray()))
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
            val user = claims
                    .body
                    .subject
            val authorities = ArrayList<GrantedAuthority>()
            (claims.body["auth"] as MutableList<*>).forEach { role -> authorities.add(SimpleGrantedAuthority(role.toString())) }

            UsernamePasswordAuthenticationToken(user, null, authorities)
        } catch (e: Exception) {
            return null
        }
    }
}
