package com.saxion.workr.models

import com.fasterxml.jackson.annotation.JsonProperty

//Appurls class
//When the app starts up we dynamically give the user all urls
//The reason we do this, is because if we ever change an url (ex. /profile/picture to /profile/image)
//we can do this, without forcing the user to update the app.
//We can simply tell the app there are new urls available, and the app will change the urls
class AppUrls(@JsonProperty("sHash") var sHash: String){
    val urls: Array<String>
        get() {
            return urls
        }

    fun checkUrls(): String{
        if (urls.hashCode().toString() == sHash){
            return "OK"
        }
        return "New urls available"
    }
}
