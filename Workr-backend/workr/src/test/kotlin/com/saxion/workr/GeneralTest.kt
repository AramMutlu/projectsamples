package com.example.demo

import com.saxion.workr.controllers.GeneralController
import com.saxion.workr.models.User
import com.saxion.workr.repositories.CompanyRepository
import com.saxion.workr.repositories.UserRepository
import com.saxion.workr.repositories.WorkerRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.mockito.Mockito

import com.fasterxml.jackson.databind.ObjectMapper
import junit.framework.Assert.assertEquals

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.core.JsonProcessingException

import com.saxion.workr.models.Company
import com.saxion.workr.models.WorkerType
import org.springframework.boot.json.JsonParseException
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc


import java.io.IOException


@AutoConfigureMockMvc
class GeneralTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Throws(JsonProcessingException::class)
    protected fun mapToJson(obj: Any): String {
        val objectMapper = ObjectMapper()
        return objectMapper.writeValueAsString(obj)
    }

    @Throws(JsonParseException::class, JsonMappingException::class, IOException::class)
    protected fun <T> mapFromJson(json: String, clazz: Class<T>): T {

        val objectMapper = ObjectMapper()
        return objectMapper.readValue(json, clazz)
    }
    val companyRepository: CompanyRepository = Mockito.mock<CompanyRepository>(CompanyRepository::class.java);

    val userRepository: UserRepository = Mockito.mock<UserRepository>(UserRepository::class.java);
    val workerRepository: WorkerRepository = Mockito.mock<WorkerRepository>(WorkerRepository::class.java);



    @Test
    fun createProduct() {
        val uri = "localhost:8080/register"
        val company = Company()
        company.code = "company"
        company.name = "company"
        val workerType = WorkerType("Employee")

        val user = User("tester", "tester","tester@mail.com","tester",company,workerType)

        val inputJson = mapToJson(user)
        val mvcResult = mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn()

        val status = mvcResult.response.status
        assertEquals(201, status)
        val content = mvcResult.response.contentAsString
        assertEquals(content, "Product is created successfully")
    }
    @Test
    fun registerUser() {

        val company = companyRepository.getByCode("abc")
        assertThat(company != null)
        val workerType = workerRepository.getByType("employee")
        assertThat(workerType != null)
        val user = User("tester", "tester","tester@mail.com","tester",company!!,workerType!!)
        val savedUser = userRepository.save(user)
        val findUser = userRepository.findByUsername("tester")
        assertThat(!findUser.isEmpty)
    }
}
