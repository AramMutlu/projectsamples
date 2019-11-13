package com.saxion.workr

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import javax.sql.DataSource
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootApplication
class WorkrApplication

fun main(args: Array<String>) {
    runApplication<WorkrApplication>(*args)
}

//Sets configuration to connect to database
@Configuration
class DatasourceConfig {
    @Bean
    fun datasource(): DataSource {
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                //.url("jdbc:mysql://myWorkr:3309/workrdb?createDatabaseIfNotExist=true&autoReconnect=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC")
                .url("jdbc:mysql://localhost:3306/workrdb?createDatabaseIfNotExist=true&autoReconnect=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC")
                .username("test")
                .password("test")
                .build()
    }
}