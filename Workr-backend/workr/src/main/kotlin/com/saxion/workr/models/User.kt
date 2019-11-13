package com.saxion.workr.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.File
import java.util.*
import javax.persistence.Id;
import javax.persistence.*


//User class
@Entity
@Table(name = "user", uniqueConstraints = [UniqueConstraint(columnNames = arrayOf("username"))])
class User(@JsonProperty("username") username: String, @JsonProperty("password") password: String,
           @JsonProperty("email") email: String, @JsonProperty("name") name: String,
           @JsonProperty("company") company: Company,
           @JsonProperty("workerType") workerType: WorkerType) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val user_id: Int = 0

    @JsonIgnore
    @Column(name = "username", nullable = false, unique = true)
    var username = ""

    @get:JsonIgnore
    @Column(name = "password", nullable = false, unique = true)
    var password = ""

    @get:JsonIgnore
    @Column(name = "ip", nullable = false, unique = true)
    var ip = ""

    //Will be converted to an .jpg when received
    //Sent as base64
    @Column(name = "picture", nullable = true, unique = true)
    var picture = ""
        get() {
            if (field == "") {
                return ""
            }
            val file = File(field)
            return Base64.getEncoder().encodeToString(file.readBytes())
        }

    @Column(name = "email")
    var email = ""

    @Column(name = "phone")
    var phone = ""

    @Column(name = "name")
    var name = ""

    @get:JsonIgnore
    @OneToMany(mappedBy = "shift_id", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    var shifts: Set<Shift>? = null

    @get:JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    var company: Company? = null

    @get:JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    var workerType: WorkerType?

    init {
        this.username = username
        this.password = password
        this.email = email
        this.name = name
        this.company = company
        this.workerType = workerType
    }
}