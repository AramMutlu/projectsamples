package com.example.workr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import okhttp3.*
import java.io.IOException

class RegisterActivity : AppCompatActivity(), Callback {

    private val client = OkHttpClient()
    val JSON = MediaType.parse("application/json; charset=utf-8")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val actionbar = supportActionBar
        actionbar!!.title = "Register"

        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        var btnRegister = findViewById<Button>(R.id.register_button)
        btnRegister.setOnClickListener {
            var name = findViewById<EditText>(R.id.register_name).text
            var username = findViewById<EditText>(R.id.register_username).text
            var password = findViewById<EditText>(R.id.register_password).text
            var password2 = findViewById<EditText>(R.id.register_password2).text
            var email = findViewById<EditText>(R.id.register_email).text
            var companycode = findViewById<EditText>(R.id.register_companycode).text

            //Convert alle the data to json
            val json = "{\"username\": \"" + username + "\" ," +
                    "\"password\": \"" + password + "\"," +
                    "\"name\": \"" + name + "\", " +
                    "\"email\": \"" + email + "\", " +
                    "\"companycode\": \"" + companycode + "\", " +
                    "\"workertype\": \"test\", " +
                    "\"type\": \"test\" }"

            apiCall(json)
        }
    }

    //Apicall to register a user in the database
    fun apiCall(json: String) {
        val body = RequestBody.create(JSON, json)

        val request = Request.Builder()
            .url(resources?.getString(R.string.URL) + resources?.getString(R.string.URL_register))
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .post(body)
            .build()

        client.newCall(request).enqueue(this)
    }

    override fun onFailure(call: Call, e: IOException) {

    }

    override fun onResponse(call: Call, response: Response) {
        //If the user is successfully registered
        if (response.isSuccessful) {
            runOnUiThread {
                Toast.makeText(
                    applicationContext,
                    "Register completed",
                    Toast.LENGTH_SHORT
                ).show()
            }
            //Start the main activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        } else {
            runOnUiThread {
                Toast.makeText(
                    applicationContext,
                    "Something went wrong, please check your input fields",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}

interface OnRequestCompleteListener {
    fun onSucces(forcast: String)
    fun onError()
}
