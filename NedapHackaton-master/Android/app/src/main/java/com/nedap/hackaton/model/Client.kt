package com.nedap.hackaton.model

import java.io.Serializable

class Client : Serializable {
    var id = System.currentTimeMillis()
    var name: String = ""
    var reports: ArrayList<Report> = arrayListOf()
}