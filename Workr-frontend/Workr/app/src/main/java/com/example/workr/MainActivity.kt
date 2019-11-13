package com.example.workr

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent

class MainActivity : AppCompatActivity() {

    //Main Activity that checks if there is an Authorization key in the Sharedpreferences
    //If there is one, it will fast forward the user to the Parent Activity with all the Tabs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val KEY_AUTHORIZATION = "token"
        //Get the Authorization key from the SharedPreferences
        val sharedPref =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val authorization = sharedPref?.getString(KEY_AUTHORIZATION, "error")

        //If there is no Authorization key, Show the main activity layout
        if (authorization == "error") {
            setContentView(R.layout.activity_main)

            var btnSign = findViewById<Button>(R.id.btnSignIn);
            btnSign.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

            var btnRegister = findViewById<Button>(R.id.btnRegister);
            btnRegister.setOnClickListener {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        } else {
            //If there is an Authorization key, Fast forward the user to the Parent Activity
            //with all the tabs
            val intent = Intent(this, ParentActivity::class.java)
            startActivity(intent)

        }
    }
}
