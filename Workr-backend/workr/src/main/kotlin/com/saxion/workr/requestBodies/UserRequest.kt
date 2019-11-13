package com.saxion.workr.requestBodies

import com.saxion.workr.models.Company
import com.saxion.workr.models.User
import com.saxion.workr.models.WorkerType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

//These are requests the request body has to fulfill before the backend
//accepts the request
class EmaiRequest(val email: String)
class PasswordRequest(val password: String)
class PictureRequest(val picture: String)
class PhoneRequest(val phone: String)
class WorkerRequest(val workerType: String)

class RegisterRequest(val companycode: String, val username: String, val password: String, val email: String, val name: String) {
   //Used to create an user
    fun getUserModel(company: Company, workerType: WorkerType): User {
        val userModel = User(username, BCryptPasswordEncoder().encode(password), email, name, company, workerType)
        return userModel
    }
}

