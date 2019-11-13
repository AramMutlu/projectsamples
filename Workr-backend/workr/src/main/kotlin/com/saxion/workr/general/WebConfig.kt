package com.saxion.workr.general

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

//Webconfig
@Configuration
class AppConfiguration {

    //Encrypt password
    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder(SecurityConstants.STRENGTH)
    }
}

//General setup, which urls to allow etc.
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebConfig(
        val bCryptPasswordEncoder: BCryptPasswordEncoder,
        val userDetailsService: AppUserDetailsService
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // no sessions
                .and()
                .authorizeRequests()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/error/**").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll() //allow
                .antMatchers(HttpMethod.POST, "/register").permitAll()
                .antMatchers(HttpMethod.POST, "/add/workertype").permitAll()
                .anyRequest().authenticated() //the rest needs authentication
                .and()
                .addFilter(JWTAuthenticationFilter(authenticationManager()))
                .addFilter(JWTAuthorizationFilter(authenticationManager()))
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder) //Encrypt password
    }

    //Setup login as provider
    @Bean
    fun authProvider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService)
        authProvider.setPasswordEncoder(bCryptPasswordEncoder)
        return authProvider
    }
}
