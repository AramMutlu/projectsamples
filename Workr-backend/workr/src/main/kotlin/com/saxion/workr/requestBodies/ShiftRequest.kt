package com.saxion.workr.requestBodies

import com.saxion.workr.models.Company
import com.saxion.workr.models.Shift
import com.saxion.workr.models.User
import java.util.*

//These are requests the request body has to fulfill before the backend
//accepts the request

class ShiftRequest(val user: String, val isOpen: Boolean, val date: Date, val start: Date, val end: Date, val company: String) {
    fun getShiftModel(user: User, company: Company): Shift {
        val shiftModel = Shift(user, isOpen, date, start, end, company)
        return shiftModel

    }
}
class AskFreeRequest(val shift_id: Int)
class CancelRequest(val shift_id: Int)