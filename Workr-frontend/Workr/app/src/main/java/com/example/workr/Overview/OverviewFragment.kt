package com.example.workr.Overview

import android.content.Context
import okhttp3.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.workr.Classes.Shift
import com.google.gson.Gson
import java.io.IOException
import com.example.workr.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class OverviewFragment : Fragment(), Callback, OverviewAdapter.RefreshClick {

    private val client = OkHttpClient()
    private val JSON = MediaType.parse("application/json; charset=utf-8")
    private var username = ""
    private var authorization = ""
    var msg = ""
    private lateinit var listView: ListView

    var wasDateClicked: Boolean = false

    //Will be called when adapter says it should refresh
    override fun refreshy() {
        activity?.runOnUiThread {
            wasDateClicked = true
            apiCall()

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    var adapter: OverviewAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_overview, container, false)

        listView = view.findViewById(R.id.overview_listView)

        //Get all the data
        apiCall()

        val calendarView = view?.findViewById<CalendarView>(R.id.calendarView)

        calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            msg = "" + year + "-" + (month + 1) + "-" + dayOfMonth

            adapter?.filter?.filter(msg)
        }
        adapter?.notifyDataSetChanged()

        return view
    }

    //Apicall to get all the data that is needed
    fun apiCall() {
        val KEY_AUTHORIZATION = "token"
        val KEY_USERNAME = "username"
        //Get the username and authorization from the device stored in SharedPreferences
        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
        authorization = sharedPref?.getString(KEY_AUTHORIZATION, "ERROR").toString()
        username = sharedPref?.getString(KEY_USERNAME, "ERROR").toString()

        val request = Request.Builder()
            .url(context?.resources?.getString(R.string.URL) + context?.resources?.getString(R.string.URL_shift))
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

            var getResponse = response.body()?.string()
            var shifts = Gson().fromJson(getResponse, Array<Shift>::class.java).toList()

            //Update the adapter to show the data
            activity?.runOnUiThread {
                adapter = OverviewAdapter(context!!, shifts, shifts, username, authorization)
                adapter?.refreshyClickListener(this)
                listView.adapter = adapter
                //Check if an date in the Calender was clicked to show the right data
                if (wasDateClicked == false) {
                    val current = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val formatted = current.format(formatter)
                    adapter?.filter?.filter(formatted)
                } else {
                    wasDateClicked = false
                    adapter?.filter?.filter(msg)
                }

            }

        } else {
            activity?.runOnUiThread {
                Toast.makeText(
                    activity,
                    "There went something wrong the Get request",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        /* if (context is OnFragmentInteractionListener) {
             listener = context
         } else {
             throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
         }*/
    }


}
