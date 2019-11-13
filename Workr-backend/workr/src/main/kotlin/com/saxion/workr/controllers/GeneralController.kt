package com.saxion.workr.controllers

import com.saxion.workr.general.error.ErrorResponse
import com.saxion.workr.general.GeneralFunction
import com.saxion.workr.models.AppUrls
import com.saxion.workr.models.User
import com.saxion.workr.models.Version
import com.saxion.workr.models.WorkerType
import com.saxion.workr.repositories.CompanyRepository
import com.saxion.workr.repositories.UserRepository
import com.saxion.workr.repositories.WorkerRepository
import com.saxion.workr.requestBodies.RegisterRequest
import com.saxion.workr.requestBodies.WorkerRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

//General controller
//Contains general functions like login, register
@RestController
@CrossOrigin
class GeneralController(@Autowired val userRepository: UserRepository,
                        @Autowired val companyRepository: CompanyRepository,
                        @Autowired val workerRepository: WorkerRepository) {

    //Gets app version
    //If app version == backend app version -> everything OK
    //else disable app
    @RequestMapping("/app/version")
    fun appVersion(@RequestBody appversion: Version): String {
        println("Received POST request : ".plus(appversion.currentVersion))
        return appversion.checkVersion()
    }

    //Gets app urls
    //Can be used to dynamically put/change urls in the app
    @RequestMapping("/app/urls")
    fun appUrls(@RequestBody appUrls: AppUrls): String {
        println("Received POST request : ".plus(appUrls))
        return appUrls.checkUrls()
    }

    //Adds workerType
    //Includes: Admin, Employer, Employee
    //User needs to be an admin
    @RequestMapping("/add/workertype")
    fun addWorkertype(@Valid @RequestHeader authorization: String, @Valid @RequestBody workerRequest: WorkerRequest): ResponseEntity<WorkerType> {
        try {
            val user = GeneralFunction(userRepository).checkBearer(authorization)

            if (user.workerType?.type?.toLowerCase() != "admin") {
                throw ErrorResponse("You are unauthorized", HttpStatus.FORBIDDEN)
            }
            val w = WorkerType(workerRequest.workerType)
            workerRepository.save(w)
            return ResponseEntity(w, HttpStatus.CREATED)

        } catch (_: DataIntegrityViolationException) {
            throw ErrorResponse("Workertype already exists", HttpStatus.CONFLICT)
        }
    }

    //POST register employer
    //Creates an employer
    //User needs to be an admin
    @RequestMapping("/register/employer")
    fun registerEmployer(@Valid @RequestHeader authorization: String, @Valid @RequestBody registerRequest: RegisterRequest): ResponseEntity<User> {
        println("Received POST request : ".plus("${registerRequest.username} ${registerRequest.password}"))

        val user = GeneralFunction(userRepository).checkBearer(authorization)

        if (user.workerType?.type?.toLowerCase() != "admin") {
            throw ErrorResponse("You are unauthorized", HttpStatus.FORBIDDEN)
        }

        try {
            val c = companyRepository.getByCode(registerRequest.companycode)
                    ?: throw ErrorResponse("Company does not exists", HttpStatus.NOT_FOUND)
            val w = workerRepository.getByType("Employer")
                    ?: throw ErrorResponse("WorkerType does not exists", HttpStatus.NOT_FOUND)

            val u = c?.let { registerRequest.getUserModel(it, w) }

            userRepository.save(u)
            return ResponseEntity(u, HttpStatus.CREATED)

        } catch (_: DataIntegrityViolationException) {
            throw ErrorResponse("An unknown error occured. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    //POST register
    //Register an employee
    //Puts IP-Adres to avoid mass creation of accounts
    @RequestMapping("/register")
    fun register(@Valid @RequestBody registerRequest: RegisterRequest, request: HttpServletRequest): ResponseEntity<User> {
        println("Received POST request : ".plus("${registerRequest.username} ${registerRequest.password}"))
        try {

            val c = companyRepository.getByCode(registerRequest.companycode)
                    ?: throw ErrorResponse("Company does not exists", HttpStatus.NOT_FOUND)
            val w = workerRepository.getByType("Employee")
                    ?: throw ErrorResponse("WorkerType does not exists", HttpStatus.NOT_FOUND)

            val u = c.let { registerRequest.getUserModel(it, w) }

            u.ip = request.remoteAddr //Get ip. When it already exists, show warning
            userRepository.save(u)
            return ResponseEntity(u, HttpStatus.CREATED)

        } catch (ex: DataIntegrityViolationException) {
            if (ex.message?.contains("user_ip_uindex")!!) { //IP adress conflict
                throw ErrorResponse("You already have an account", HttpStatus.CONFLICT)
            } else {
                throw ErrorResponse("An unknown error occured. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }
    }
}