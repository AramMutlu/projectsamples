package com.nedap.hackaton.model

import com.nedap.hackaton.model.Constants.BLOOD
import com.nedap.hackaton.model.Constants.BODY_TEMPERATURE
import com.nedap.hackaton.model.Constants.WEIGHT
import java.io.Serializable
import java.util.*

class Report : Serializable {
    var id = System.currentTimeMillis()
    var text: String = "Test"
    var date: Date = Date()
    var measurements = hashMapOf<String, Any?>(
        BLOOD to null,
        BODY_TEMPERATURE to null,
        WEIGHT to null
    )
}