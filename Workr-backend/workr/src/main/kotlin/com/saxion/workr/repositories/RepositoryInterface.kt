package com.saxion.workr.repositories

import com.saxion.workr.models.Company
import com.saxion.workr.models.Shift
import com.saxion.workr.models.User
import com.saxion.workr.models.WorkerType
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*

//These are repositories to get objects from the database

@Transactional
@Component
interface UserRepository : CrudRepository<User, Int> {
    fun getByUsername(username: String?): User?
    fun findByUsername(username: String): Optional<User>
}

@Transactional
@Component
interface CompanyRepository : CrudRepository<Company, Int> {
    fun getByCode(companycode: String?): Company?
}

@Transactional
@Component
interface ShiftRepository : CrudRepository<Shift, Int>

@Transactional
@Component
interface WorkerRepository : CrudRepository<WorkerType, Int> {
    fun getByType(type: String?): WorkerType?
}