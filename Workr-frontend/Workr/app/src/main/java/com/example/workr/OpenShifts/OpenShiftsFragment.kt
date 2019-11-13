package com.example.workr.OpenShifts

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import com.example.workr.Classes.Shift
import com.example.workr.R
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [OpenShiftsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [OpenShiftsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OpenShiftsFragment : Fragment(), Callback, OpenShiftsAdapter.RefreshClick {
    private val client = OkHttpClient()
    private val JSON = MediaType.parse("application/json; charset=utf-8")
    private var username = ""
    private var authorization = ""

    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    var adapter: OpenShiftsAdapter? = null

    //Will be called when adapter says it should refresh
    override fun refreshy() {
        activity?.runOnUiThread {
            //Get all the data
            apiCall()

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {//

        var view = inflater.inflate(R.layout.fragment_openshifts, container, false)
        listView = view.findViewById(R.id.openShifts_listView)

        apiCall()
        adapter?.notifyDataSetChanged()

        return view
    }

    fun refresh() {
        fragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()
        activity?.runOnUiThread {
            adapter?.notifyDataSetInvalidated()
            apiCall()
            adapter?.notifyDataSetChanged()
        }
    }

    //Apicall to get all the data that is needed
    fun apiCall() {
        val KEY_AUTHORIZATION = "token"
        val KEY_USERNAME = "username"
        //Get the username and authorization key from the device stored in SharedPreferences
        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
        authorization = sharedPref?.getString(KEY_AUTHORIZATION, "ERROR").toString()
        username = sharedPref?.getString(KEY_USERNAME, "ERROR").toString()

        val request = Request.Builder()
            .url(context?.resources?.getString(R.string.URL) + context?.resources?.getString(R.string.URL_shift_open))
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

            var openShifts = Gson().fromJson(getResponse, Array<Shift>::class.java).toList()
            var sortedList = openShifts.sortedBy { it -> it.date }

            //Update the adapter to show the data
            activity?.runOnUiThread {
                adapter = OpenShiftsAdapter(context!!, sortedList, username, authorization)
                adapter?.refreshyClickListener(this)
                listView.adapter = adapter
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
