package com.saxion.workr.controllers
import com.saxion.workr.general.error.ErrorResponse
import com.saxion.workr.general.GeneralFunction
import com.saxion.workr.models.Shift
import com.saxion.workr.repositories.CompanyRepository
import com.saxion.workr.repositories.ShiftRepository
import com.saxion.workr.repositories.UserRepository
import com.saxion.workr.requestBodies.AskFreeRequest
import com.saxion.workr.requestBodies.CancelRequest
import com.saxion.workr.requestBodies.ShiftRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

//Shift controller
//Includes all shift related functions
@RestController
@CrossOrigin
class ShiftController(@Autowired val shiftRepository: ShiftRepository,@Autowired val userRepository: UserRepository, @Autowired val companyRepository: CompanyRepository) {

//    {
//        "username": "username1",
//        "isOpen" : false,
//        "start": "2019-10-01T09:45:00.000+02:00",
//        "end": "2019-10-01T09:45:00.000+02:00"
//    }
    //POST shift
    //Creates and adds a shift
    @RequestMapping("/shift", method= [RequestMethod.POST])
    fun postShift(@Valid @RequestBody shiftRequest: ShiftRequest): ResponseEntity<Shift> {
        try {
            val u = userRepository.getByUsername(shiftRequest.user)
                    ?: throw ErrorResponse("User does not exist", HttpStatus.NOT_FOUND)

            if (u.workerType?.type?.toLowerCase() != "employer") {
                throw ErrorResponse("You are unauthorized", HttpStatus.FORBIDDEN)
            }

            val c = companyRepository.getByCode(shiftRequest.company)
                    ?: throw ErrorResponse("Company does not exist", HttpStatus.NOT_FOUND)

            val s = shiftRequest.getShiftModel(u, c)
            shiftRepository.save(s)
            return ResponseEntity(s, HttpStatus.CREATED)
        }catch (_: DataIntegrityViolationException) {
            throw ErrorResponse("Something went wrong while adding the shift", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    //    {
//        "username": "username1",
//        "password": "test",
//        "email": "test@test.nl",
//        "name": "tester",
//        "workerType":"test",
//        "type": "test"
//    }

    //GET shift
    //Returns a list of shifts
    @RequestMapping("/shift", method= [RequestMethod.GET])
    fun getShifts(@Valid @RequestHeader("Authorization") authorization : String): List<Shift> {
        println("Received GET request : ".plus(authorization))
        try {
            val code = GeneralFunction(userRepository).checkBearer(authorization).company?.code
            return shiftRepository. findAll().filter { s -> s.company?.code == code}
        } catch (_: DataIntegrityViolationException) {
            throw ErrorResponse("Something went wrong while getting the shift", HttpStatus.INTERNAL_SERVER_ERROR)
        }

    }

    //GET shifts of an user
    //Requires username
    @RequestMapping("/shift/{username}", method= [RequestMethod.GET])
    fun getShifts(@Valid @RequestHeader("Authorization") authorization : String, @PathVariable username: String): List<Shift> {
        println("Received GET request : ".plus(authorization))
        try {
            val code =  GeneralFunction(userRepository).checkBearer(authorization).company?.code
            return shiftRepository.findAll().filter { s -> s.company?.code == code && s.user?.username == username}
        } catch (_: DataIntegrityViolationException) {
            throw ErrorResponse("Something went wrong while getting the shift", HttpStatus.INTERNAL_SERVER_ERROR)
        }

    }

    //Gets open shifts
    //Call is used for hybrid app
    @RequestMapping("/shift/open/grouped", method = [RequestMethod.GET])
    fun getOpenShiftsGrouped(@RequestHeader("Authorization") authorization: String): Map<String?, List<Shift>> {//: ResponseEntity<Array<Shift>> {
        println("Received GET request: openShifts")

        try {
            val companyid = GeneralFunction(userRepository).checkBearer(authorization).company?.company_id
            return shiftRepository.findAll().filter { s -> s.is_open && s.company?.company_id == companyid}.groupBy { s -> s.getDateFormatted(true) }
        } catch (_: DataIntegrityViolationException){
            throw ErrorResponse("There went something wrong with getting the open shifts", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    //GET open shift
    //Call is used by Native android app
    @RequestMapping("/shift/open", method = [RequestMethod.GET])
    fun getOpenShifts(@RequestHeader("Authorization") authorization: String): List<Shift> {//: ResponseEntity<Array<Shift>> {
        println("Received GET request: openShifts")

        try {
            val companyid =  GeneralFunction(userRepository).checkBearer(authorization).company?.company_id
            return shiftRepository.findAll().filter { s -> s.is_open && s.company?.company_id == companyid}
        } catch (_: DataIntegrityViolationException){
            throw ErrorResponse("There went something wrong with getting the open shifts", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    //PUT open shift
    //Changes shift into an open shift
    @PutMapping("/shift/askfree")
    fun askFree(@Valid @RequestBody body: AskFreeRequest): Shift {
        println("Received PUT request: askfree ".plus(body))
        try {
            val s = shiftRepository.findAll().filter { s -> s.shift_id == body.shift_id }.first()
            s.is_open = true
            return shiftRepository.save(s)
        } catch (_: DataIntegrityViolationException){
            throw ErrorResponse("Something went wrong while requesting a free day", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    //PUT SHIFT
    //Changes open shift to normal shift
    @RequestMapping("/shift/cancelRequest", method = [RequestMethod.PUT])
    fun cancelRequest( @RequestBody body: CancelRequest): Shift {
        println("Received PUT request: cancelRequest ".plus(body))
        try {
            val s = shiftRepository.findAll().filter { s -> s.shift_id == body.shift_id }.first()
            s.is_open = false
            return shiftRepository.save(s)
        } catch (_: DataIntegrityViolationException){
            throw ErrorResponse("There went something wrong with canceling the request", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    //PUT take shift
    //Changes the user of a shift
    @RequestMapping("/shift/takeover", method = [RequestMethod.PUT])
    fun fill(@RequestHeader("Authorization") authorization: String, @RequestBody body: AskFreeRequest): Shift {
        println("Received PUT request: takeover ".plus(body.shift_id))
        try{
            val u =  GeneralFunction(userRepository).checkBearer(authorization)

            val s = shiftRepository.findAll().filter { s -> s.shift_id == body.shift_id }.first()
                    ?: throw ErrorResponse("Shift not found", HttpStatus.NOT_FOUND)
            val se = shiftRepository.findAll().filter { se -> se.date == s.date && se.user?.username == u.username}
            if(se.isEmpty()){
                s.user = u
                s.is_open = false
                return shiftRepository.save(s)
            }
            throw ErrorResponse("You already are working that day", HttpStatus.CONFLICT)
        } catch (_: DataIntegrityViolationException){
            throw ErrorResponse("There went something wrong with taking the shift over", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

}