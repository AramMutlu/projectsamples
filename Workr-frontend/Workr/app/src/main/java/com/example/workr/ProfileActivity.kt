package com.example.workr

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.workr.Classes.Shift
import com.example.workr.Classes.User
import com.example.workr.Overview.OverviewAdapter
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.util.*
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class ProfileActivity : AppCompatActivity(), Callback, OverviewAdapter.RefreshClick {

    private val client = OkHttpClient()
    private val JSON = MediaType.parse("application/json; charset=utf-8")
    private var username = ""
    private var this_username = ""
    private var authorization = ""
    private lateinit var listView: ListView
    private var user: User? = null
    private val REQUEST_CODE_PERMISSIONS = 101
    private val REQUIRED_PERMISSIONS =
        arrayOf("android.permission.CALL_PHONE")

    var adapter: OverviewAdapter? = null

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val actionbar = supportActionBar
        actionbar!!.title = "Profile"

        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        this_username = intent.getStringExtra("username").toString()

        //Get the data of the user
        apiCall_profile()

        val btn_phoneNumber = findViewById<TextView>(R.id.activity_profile_textView_phoneNumber)
        btn_phoneNumber.setOnClickListener {
            //Check is the Permissions are granted
            if (allPermissionsGranted()) {
                //Start the ACTION_CALL Intent
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "${user?.phone}"))
                startActivity(intent)
            } else {
                //Request the permissions
                ActivityCompat.requestPermissions(
                    this,
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            }
        }

        val btn_email = findViewById<TextView>(R.id.activity_profile_textView_email)
        btn_email.setOnClickListener {
            //Check is the Permissions are granted
            if (allPermissionsGranted()) {
                //Start the ACTION_SENDTO Intent (email)
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.setData(Uri.parse("mailto:${user?.email}"))
                startActivity(intent)
            } else {
                //Request the permissions
                ActivityCompat.requestPermissions(
                    this,
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            }
        }

        listView = findViewById(R.id.activity_profile_listView)
        //Get the data that is needed for the listview
        apiCall()
    }

    override fun refreshy() {
        apiCall()
    }

    //Apicall to get the user data
    fun apiCall_profile() {
        //Get the authorization key from the device stored in SharedPreferences
        val KEY_AUTHORIZATION = "token"
        val sharedPref =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        authorization = sharedPref?.getString(KEY_AUTHORIZATION, "ERROR").toString()

        var username = intent.getStringExtra("username")
        val request = Request.Builder()
            .url(resources?.getString(R.string.URL) + resources?.getString(R.string.URL_profile) + "/${username}")
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("Authorization", authorization)
            .get()
            .build()

        client.newCall(request).enqueue(this)

    }

    //Apicall to get the user data from the database
    fun apiCall() {
        val KEY_AUTHORIZATION = "token"
        val KEY_USERNAME = "username"
        //Get the username and authorization from the device stored in SharedPreferences
        val sharedPref =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        authorization = sharedPref?.getString(KEY_AUTHORIZATION, "ERROR").toString()
        username = sharedPref?.getString(KEY_USERNAME, "ERROR").toString()

        val request = Request.Builder()
            .url(resources?.getString(R.string.URL) + resources?.getString(R.string.URL_shift) + "/${this_username}")
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("Authorization", authorization)
            .get()
            .build()

        client.newCall(request).enqueue(this)

    }

    override fun onFailure(call: Call, e: IOException) {

    }

    override fun onResponse(call: Call, response: Response) {
        if (response.isSuccessful) {
            //Check is the response is from "/shift/username"
            if (response.request().url().toString() == resources?.getString(R.string.URL) + resources?.getString(
                    R.string.URL_shift
                ) + "/${this_username}"
            ) {
                var getResponse = response.body()?.string()
                var shifts = Gson().fromJson(getResponse, Array<Shift>::class.java).toList()

                //Initialize the Adapter and Connect it to the the listview adapter
                adapter = OverviewAdapter(this, shifts, shifts, username, authorization)
                runOnUiThread {
                    listView.adapter = adapter
                }
            }

            if (response.request().url().toString() == resources?.getString(R.string.URL) + "/profile/${this_username}") {
                var getResponse = response.body()?.string()
                user = Gson().fromJson(getResponse, User::class.java)

                //Decode the picture and display all the user's information
                val imageByteArray = Base64.getDecoder().decode(user?.picture)
                val bmp = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
                if (user?.picture != "") {
                    findViewById<ImageView>(R.id.activity_profile_image).setImageBitmap(bmp)
                } else {
                    findViewById<ImageView>(R.id.activity_profile_image).setImageResource(R.mipmap.ic_launcher)
                }
                findViewById<TextView>(R.id.activity_profile_textView_phoneNumber).text =
                    user?.phone
                findViewById<TextView>(R.id.activity_profile_textView_email).text = user?.email
                findViewById<TextView>(R.id.activity_profile_textView_name).text = user?.name
            }

        } else {
            Toast.makeText(this, "There went something wrong the Get request", Toast.LENGTH_SHORT)
                .show()
        }
    }

    //Check if permissions are granted
    private fun allPermissionsGranted(): Boolean {

        for (permission in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        //Request the Permissions
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + user?.phone))
                startActivity(intent)
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
