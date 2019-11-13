package com.saxion.workr.models

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.persistence.*
import javax.persistence.JoinColumn

//Shift class
//If open, shift is available to take over
@Entity
@Table(name = "shift")
class Shift(@JsonProperty("user") user: User, @JsonProperty("isOpen") isOpen: Boolean, @JsonProperty("date") date: Date, @JsonProperty("start") start: Date, @JsonProperty("end") end: Date, @JsonProperty("company") company: Company) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var shift_id: Int = 0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    var user: User? = null //mapping error

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @get:JsonIgnore
    var company: Company? = null //mapping error

    var is_open: Boolean = false

    @get:JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-dd")
    var date: Date? = null

    //Format date to ex. 11-12-2019
    //Used because we're grouping shifts, which is easier to put in an list
    fun getDateFormatted(full: Boolean): String? {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0") //See .0

        val formatter: DateTimeFormatter = if (full) {
            DateTimeFormatter.ofPattern("dd MMMM yyyy")
        } else {
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        }

        val localStartDate = LocalDate.parse(this.date.toString(), inputFormatter)
        return localStartDate.format(formatter)
    }

    //1:00 PM
    @JsonFormat(pattern = "hh:mm a")
    var start: Date? = null

    @JsonFormat(pattern = "hh:mm a")
    var end: Date? = null

    init {
        this.user = user
        this.company = company
        this.is_open = isOpen
        this.date = date
        this.start = start
        this.end = end
    }
}