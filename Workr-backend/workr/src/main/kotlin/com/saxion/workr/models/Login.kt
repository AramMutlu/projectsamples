package com.saxion.workr.models

import com.fasterxml.jackson.annotation.JsonProperty

//Used when loggin in
//Needed for authorization class
class Login(@JsonProperty("username") val username: String, @JsonProperty("password") val password: String) {
}