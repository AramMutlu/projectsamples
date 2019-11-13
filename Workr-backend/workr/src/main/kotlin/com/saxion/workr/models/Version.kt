package com.saxion.workr.models

import com.fasterxml.jackson.annotation.JsonProperty

//Version class
//Checks if the app needs an update
//If the app needs an update, we need to (soft) block the app usage
//till the user updates his/hers app
class Version(@JsonProperty("version") var appVersion: String) {
    val currentVersion = "1.0.0"

    fun checkVersion(): String {
        if (currentVersion == appVersion) {
            return "OK"
        } else {
            return "NOT OK"
        }
    }
}
