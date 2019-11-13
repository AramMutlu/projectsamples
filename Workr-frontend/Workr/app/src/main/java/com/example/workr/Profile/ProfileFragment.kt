package com.example.workr.Profile

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import androidx.room.Room
import com.example.workr.CameraActivity
import com.example.workr.Classes.User
import com.example.workr.Database.AppDatabase
import com.example.workr.Database.NotificantionDao
import com.example.workr.Database.Notification
import com.example.workr.R
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.util.*

class ProfileFragment : Fragment(), Callback {

    private val client = OkHttpClient()
    private val JSON = MediaType.parse("application/json; charset=utf-8")
    var authorization = ""
    val KEY_NOTIFICATIONS_OPENSHIFTS = "keyNotificationsOpenShifts"
    val KEY_NOTIFICATIONS_UNFILLEDSHIFTS = "keyNotificationsUnfilledShifts"
    val KEY_NOTIFICATIONS_EMAIL = "keyNotificationsEmail"

    //Create the local Database
    private var db: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = AppDatabase.getInstance(context!!)
    }

    //Refresh the fragment
    fun refresh() {
        fragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()
    }

    var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Get all the data
        apiCall()

        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_profile, container, false)

        //Get the data from the database to show if notifications are on or off
        AsyncTask.execute {
            val a = db?.notificationsDao()?.getAll()
            val openShiftAlert =
                a?.find { it.notificationName == KEY_NOTIFICATIONS_OPENSHIFTS }?.isOn
            val filledShiftAlert =
                a?.find { it.notificationName == KEY_NOTIFICATIONS_UNFILLEDSHIFTS }?.isOn
            val emailAlert = a?.find { it.notificationName == KEY_NOTIFICATIONS_EMAIL }?.isOn

            //Update the components in the view
            activity?.runOnUiThread {
                if (openShiftAlert != null)
                    view?.findViewById<Switch>(R.id.profile_switch_openShiftsAlerts)?.isChecked =
                        openShiftAlert

                if (filledShiftAlert != null)
                    view?.findViewById<Switch>(R.id.profile_switch_unfilledShiftAlerts)?.isChecked =
                        filledShiftAlert


                if (emailAlert != null)
                    view?.findViewById<Switch>(R.id.profile_switch_emailAlerts)?.isChecked =
                        emailAlert
            }
        }

        //Camera Button to start the Camera activity
        var imageButton = view?.findViewById<ImageButton>(R.id.activity_profile_image)
        imageButton?.setOnClickListener { view ->
            val intent = Intent(context, CameraActivity::class.java)
            startActivity(intent)
        }


        var switch_openShiftsAlerts =
            view?.findViewById<Switch>(R.id.profile_switch_openShiftsAlerts)
        switch_openShiftsAlerts?.setOnClickListener { view ->
            if (switch_openShiftsAlerts.isChecked) {
                // Turn notifications on
                changeSetting(1, true)
            } else {
                // Turn notifications off
                changeSetting(1, false)
            }
        }

        var switch_unfilledShiftsAlerts =
            view?.findViewById<Switch>(R.id.profile_switch_unfilledShiftAlerts)
        switch_unfilledShiftsAlerts?.setOnClickListener { view ->
            if (switch_unfilledShiftsAlerts.isChecked) {
                // Turn notifications on
                changeSetting(2, true)
            } else {
                // Turn notifications off
                changeSetting(2, false)
            }
        }
        var switch_emailAlerts = view?.findViewById<Switch>(R.id.profile_switch_emailAlerts)
        switch_emailAlerts?.setOnClickListener { view ->
            if (switch_emailAlerts.isChecked) {
                // Turn notifications on
                changeSetting(3, true)

            } else {
                // Turn notifications off
                changeSetting(3, false)
            }
        }

        var btn_changePassword = view?.findViewById<Button>(R.id.profile_button_changePassword)
        btn_changePassword?.setOnClickListener { view ->
            //open modal
            var modal = layoutInflater.inflate(R.layout.dialog_password_layout, null)
            var alertDialog = AlertDialog.Builder(context!!)
            alertDialog.setTitle("Change Password")
            alertDialog.setCancelable(false)

            var editText = view.findViewById<EditText>(R.id.dialog_password)

            alertDialog.setPositiveButton("Update") { dialog, which ->
                // Do something when user press the positive button
                apiCall_changePassword(editText.toString())
                activity?.runOnUiThread {
                    Toast.makeText(
                        context,
                        "Password Updated",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                //Refresh the page
                refresh()
            }
            alertDialog.setNeutralButton("Cancel") { dialog, which ->
                activity?.run { Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show() }
            }

            alertDialog.setView(modal)
            alertDialog.show()

        }

        var btn_changeEmail = view?.findViewById<Button>(R.id.profile_button_changeEmail)
        btn_changeEmail?.setOnClickListener { view ->
            //open modal
            var modal = layoutInflater.inflate(R.layout.dialog_email_layout, null)
            var alertDialog = AlertDialog.Builder(context!!)
            alertDialog.setTitle("Change Email")
            alertDialog.setCancelable(false)


            alertDialog.setPositiveButton("Update") { dialog, which ->
                // Do something when user press the positive button

                var editText = modal.findViewById<EditText>(R.id.dialog_email).text.toString()
                apiCall_changeEmail(editText)
                activity?.runOnUiThread {
                    Toast.makeText(
                        context,
                        "Email Updated",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                //Refresh the page
                refresh()
            }
            alertDialog.setNeutralButton("Cancel") { dialog, which ->
                activity?.run { Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show() }
            }

            alertDialog.setView(modal)
            alertDialog.show()


        }
        var btn_changePhone = view?.findViewById<Button>(R.id.profile_button_changePhoneNumber)
        btn_changePhone?.setOnClickListener { view ->
            //open modal
            var modal = layoutInflater.inflate(R.layout.dialog_phone_layout, null)
            var alertDialog = AlertDialog.Builder(context!!)
            alertDialog.setTitle("Update Phone Number")
            alertDialog.setCancelable(false)

            alertDialog.setPositiveButton("Update") { dialog, which ->
                // Do something when user press the positive button

                var editText = modal.findViewById<EditText>(R.id.dialog_phone).text.toString()
                apiCall_changePhone(editText)
                activity?.runOnUiThread {
                    Toast.makeText(
                        context,
                        "Phone number Updated",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                //Refresh the page
                refresh()
            }
            alertDialog.setNeutralButton("Cancel") { dialog, which ->
                activity?.run { Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show() }
            }

            alertDialog.setView(modal)
            alertDialog.show()

        }

        return view
    }

    //Function to change the Notifications preferences in the local device database
    fun changeSetting(switch: Int, boolean: Boolean) {

        var notification = Notification(1, KEY_NOTIFICATIONS_OPENSHIFTS, boolean)

        when (switch) {
            1 -> {
                notification = Notification(1, KEY_NOTIFICATIONS_OPENSHIFTS, boolean)
            }
            2 -> {
                notification = Notification(2, KEY_NOTIFICATIONS_UNFILLEDSHIFTS, boolean)
            }
            3 -> {
                notification = Notification(3, KEY_NOTIFICATIONS_EMAIL, boolean)
            }
        }
        AsyncTask.execute {
            db?.notificationsDao()?.insert(notification)
        }
    }

    //Apicall to get all the data that is needed
    fun apiCall() {
        val KEY_AUTHORIZATION = "token"
        //Get the username and authorization from the device stored in SharedPreferences
        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
        authorization = sharedPref?.getString(KEY_AUTHORIZATION, "ERROR").toString()

        val request = Request.Builder()
            .url(this.context?.resources?.getString(R.string.URL) + resources?.getString(R.string.URL_profile))
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("Authorization", authorization)
            .get()
            .build()

        client.newCall(request).enqueue(this)

    }

    //Apicall to change the password of the user
    fun apiCall_changePassword(password: String) {
        val json = "{\"password\": \"$password\" }"
        val body = RequestBody.create(JSON, json)

        val request = Request.Builder()
            .url(context?.resources?.getString(R.string.URL) + resources?.getString(R.string.URL_user_password))
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("Authorization", authorization)
            .put(body)
            .build()

        client.newCall(request).enqueue(this)

    }

    //Apicall to change the phonenumber of the user
    fun apiCall_changePhone(phone: String) {
        val json = "{\"phone\": \"$phone\" }"
        val body = RequestBody.create(JSON, json)

        val request = Request.Builder()
            .url(context?.resources?.getString(R.string.URL) + resources?.getString(R.string.URL_user_phone))
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("Authorization", authorization)
            .put(body)
            .build()

        client.newCall(request).enqueue(this)

    }

    //Apicall to change the email of the user
    fun apiCall_changeEmail(email: String) {
        val json = "{\"email\": \"$email\" }"
        val body = RequestBody.create(JSON, json)

        val request = Request.Builder()
            .url(context?.resources?.getString(R.string.URL) + resources?.getString(R.string.URL_user_email))
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("Authorization", authorization)
            .put(body)
            .build()

        client.newCall(request).enqueue(this)

    }

    override fun onFailure(call: Call, e: IOException) {

    }

    override fun onResponse(call: Call, response: Response) {
        if (response.isSuccessful) {

            //Check if the respsonse is from "/profile"
            if (response.request().url().toString() == context?.resources?.getString(R.string.URL) + resources?.getString(
                    R.string.URL_profile
                )
            ) {
                //Convert the data to a User
                var getResponse = response.body()
                user = Gson().fromJson(getResponse?.charStream(), User::class.java)

                //Update the view Components with the user data from the database
                activity?.runOnUiThread {
                    if (user?.picture != "") {
                        val imageByteArray = Base64.getDecoder().decode(user?.picture)
                        val bmp =
                            BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
                        view?.findViewById<ImageButton>(R.id.activity_profile_image)
                            ?.setImageBitmap(bmp)
                    } else {
                        view?.findViewById<ImageButton>(R.id.activity_profile_image)
                            ?.setImageResource(R.mipmap.ic_launcher)
                    }

                    view?.findViewById<TextView>(R.id.activity_profile_textView_name)?.text =
                        user?.name
                    view?.findViewById<TextView>(R.id.activity_profile_textView_email)?.text =
                        user?.email
                    view?.findViewById<TextView>(R.id.activity_profile_textView_phoneNumber)?.text =
                        user?.phone
                }

            }

        } else {
            Toast.makeText(
                activity,
                "There went something wrong the Get request",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
