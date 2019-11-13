package com.saxion.workr.controllers

import com.saxion.workr.general.error.ErrorResponse
import com.saxion.workr.general.GeneralFunction
import com.saxion.workr.models.Company
import com.saxion.workr.repositories.CompanyRepository
import com.saxion.workr.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

//Company controller.
//GET /company/{companyid}
//POST /company
@RestController
@CrossOrigin
class CompanyController(@Autowired val companyRepository: CompanyRepository, @Autowired val userRepository: UserRepository) {

    //Creates an company
    //User need to be an admin
    @RequestMapping("/company")
    fun postCompany(@Valid @RequestHeader authorization: String, @Valid @RequestBody company: Company): ResponseEntity<Company> {
        val user= GeneralFunction(userRepository).checkBearer(authorization)

        if (user.workerType?.type?.toLowerCase() != "admin") {
            throw ErrorResponse("You are unauthorized", HttpStatus.FORBIDDEN)
        }

        try {
            companyRepository.save(company)
        } catch (_: DataIntegrityViolationException) {
            throw ErrorResponse("Company already in use.", HttpStatus.CONFLICT)
        }
        return ResponseEntity(company, HttpStatus.CREATED)
    }

    //Gets an company by companycode
    //Requires bearer authorization
    @RequestMapping("/company/{companycode}", method= [RequestMethod.GET])
    fun getCompany(@PathVariable companycode: String): Company {
        return try {
            val company = companyRepository.getByCode( companycode)
            if(company == null){
                throw ErrorResponse("Company not found", HttpStatus.NOT_FOUND)
            }
             company

        } catch (_: DataIntegrityViolationException) {
            throw ErrorResponse("Something went wrong while getting your company", HttpStatus.FORBIDDEN)
        }
    }
}