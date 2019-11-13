package com.saxion.workr.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*


//WorkerType
//Whether the user is an employee, employer or admin
@Entity
@Table(name = "workertype")
class WorkerType(@JsonProperty("type") type: String) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val worker_id: Int = 0

    @JsonIgnore
    @Column(name = "type", nullable = false, unique = true)
    var type = "Employee"

    @get:JsonIgnore
    @OneToMany(mappedBy = "user_id", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    var users: Set<User>? = null

    init {
        this.type = type
    }
}