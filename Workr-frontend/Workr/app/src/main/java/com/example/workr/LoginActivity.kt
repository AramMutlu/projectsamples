package com.example.workr

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.edit
import okhttp3.*
import java.io.IOException
import okhttp3.RequestBody
import okhttp3.OkHttpClient


class LoginActivity : AppCompatActivity(), Callback {

    private val client = OkHttpClient()
    val JSON = MediaType.parse("application/json; charset=utf-8")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val actionbar = supportActionBar
        actionbar!!.title = "Sign in"

        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        var btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            var username = findViewById<EditText>(R.id.login_username).text
            var password = findViewById<EditText>(R.id.login_password).text

            val json = "{\"username\": \"" + username + "\" ," +
                    "\"password\": \"" + password + "\" }"

            apiCall(json)

            //Store the username on the device in SharedPreferences
            val sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE
            )
            val KEY_USERNAME = "username"
            sharedPref.edit {
                putString(KEY_USERNAME, username.toString())
            }
        }
    }

    //Apicall to login
    fun apiCall(json: String) {
        val body = RequestBody.create(JSON, json)

        val request = Request.Builder()
            .url(resources?.getString(R.string.URL) + resources?.getString(R.string.URL_login))
            .header("Content-Type", "application/json")
            .post(body)
            .build()

        client.newCall(request).enqueue(this)

    }

    override fun onFailure(call: Call, e: IOException) {

    }

    override fun onResponse(call: Call, response: Response) {
        if (response.isSuccessful) {
            //Get the Authorization key from the header
            val token = response.header("Authorization")
            //Open the sharedpreferences file
            val sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE
            )
            val KEY_AUTHORIZATION = "token"
            //Add the Authorization key to the Sharedpreferences
            sharedPref.edit {
                putString(KEY_AUTHORIZATION, token)
                commit()
            }
            //Start the ParentActivity (the host activity with all the tab fragments)
            val intent = Intent(this, ParentActivity::class.java)
            startActivity(intent)

        } else {
            runOnUiThread {
                Toast.makeText(
                    applicationContext,
                    "Invalid Username or Password",
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

