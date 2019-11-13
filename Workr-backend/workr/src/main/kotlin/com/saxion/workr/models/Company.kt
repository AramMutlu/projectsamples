package com.saxion.workr.models

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

//Company class
//User needs an company code to register
@Entity
@Table(name = "company")
class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var company_id: String? = null

    var name: String? = null
    var code: String? = null
    @get:JsonIgnore
    @OneToMany(mappedBy = "user_id", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
    var users: Set<User>? = null

    @OneToMany(mappedBy = "shift_id", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
    var shifts: Set<Shift>? = null
}