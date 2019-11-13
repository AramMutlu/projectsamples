package com.saxion.workr.controllers

import com.saxion.workr.general.error.ErrorResponse
import com.saxion.workr.general.GeneralFunction
import com.saxion.workr.models.User
import com.saxion.workr.repositories.UserRepository
import com.saxion.workr.requestBodies.EmaiRequest
import com.saxion.workr.requestBodies.PasswordRequest
import com.saxion.workr.requestBodies.PhoneRequest
import com.saxion.workr.requestBodies.PictureRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import java.io.File
import javax.validation.Valid
import java.util.*
import java.io.FileOutputStream

@RestController
@CrossOrigin
class UserController(@Autowired val userRepository: UserRepository) {

    //GET for profile
    //Returns User object
    @RequestMapping("/profile/{username}", method = [RequestMethod.GET])
    fun getProfile(@Valid @RequestHeader authorization: String, @PathVariable username: String): User? {
        println("Received GET request : ".plus("$authorization"))
        try {
            return userRepository.getByUsername(username)
                    ?: throw ErrorResponse("User does not exist", HttpStatus.NOT_FOUND)
        } catch (_: DataIntegrityViolationException) {
            throw ErrorResponse("Something went wrong while getting a profile", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    //GET for profile
    //Returns User object of logged in user
    @RequestMapping("/profile")
    fun getOwnProfile(@Valid @RequestHeader authorization: String): User? {
        println("Received GET request : ".plus("$authorization"))
        try {
            return GeneralFunction(userRepository).checkBearer(authorization)
        } catch (_: DataIntegrityViolationException) {
            throw ErrorResponse("Something went wrong while getting your profile.", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    //PUT Request to change user's password
    //Returns updated User object
    @RequestMapping("/user/password")
    fun putPassword(@Valid @RequestHeader authorization: String, @RequestBody request: PasswordRequest): ResponseEntity<User> {

        try {
            val user = GeneralFunction(userRepository).checkBearer(authorization)
            user.password = BCryptPasswordEncoder().encode(request.password)
            userRepository.save(user)

            return ResponseEntity(user, HttpStatus.CREATED)
        } catch (_: DataIntegrityViolationException) {
            throw ErrorResponse("Something went wrong while saving your password", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    //PUT Request to change user's picture
    //Stores picture as .jpg file
    //Returns updated User object
    @RequestMapping("/user/picture")
    fun putPicture(@Valid @RequestHeader authorization: String, @RequestBody request: PictureRequest): ResponseEntity<User> {

        try {
            val user = GeneralFunction(userRepository).checkBearer(authorization)
            val imageByte = Base64.getDecoder().decode(request.picture)

            //This will decode the String which is encoded by using Base64 class
            var directory = this.javaClass.protectionDomain.codeSource.location.path + "images/"
            File(directory).mkdir()
            directory = directory + user.username + ".jpg"
            File(directory).createNewFile()

            FileOutputStream(directory).write(imageByte)

            user.picture = directory

            userRepository.save(user)

            return ResponseEntity(user, HttpStatus.CREATED)
        } catch (_: DataIntegrityViolationException) {
            throw ErrorResponse("Something went wrong while saving your picture", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    //PUT Request to change user's email
    //Returns updated User object
    @RequestMapping("/user/email")
    fun putEmail(@Valid @RequestHeader authorization: String, @RequestBody request: EmaiRequest): ResponseEntity<User> {
        try {
            val user = GeneralFunction(userRepository).checkBearer(authorization)
            user.email = request.email
            userRepository.save(user)
            return ResponseEntity(user, HttpStatus.CREATED)
        } catch (_: DataIntegrityViolationException) {
            throw ErrorResponse("Something went wrong while saving your email", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    //PUT Request to change user's phone number
    //Returns updated User object
    @RequestMapping("/user/phone")
    fun putPhone(@Valid @RequestHeader authorization: String, @RequestBody request: PhoneRequest): ResponseEntity<User> {
        try {
            val user = GeneralFunction(userRepository).checkBearer(authorization)
            user.phone = request.phone
            userRepository.save(user)
            return ResponseEntity(user, HttpStatus.CREATED)
        } catch (_: DataIntegrityViolationException) {
            throw ErrorResponse("Something went wrong while saving your phone number", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

}