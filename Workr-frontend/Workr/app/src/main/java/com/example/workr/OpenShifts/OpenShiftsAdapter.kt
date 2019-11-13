package com.example.workr.OpenShifts

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.workr.Classes.Shift
import com.example.workr.ProfileActivity
import com.example.workr.R
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.util.*

//Adapter to handle everything with Open Shifts
class OpenShiftsAdapter(
    private val context: Context,
    private var dataSource: List<Shift>,
    private var username: String,
    private var authorization: String
) : BaseAdapter(), Callback {

    private val client = OkHttpClient()
    val JSON = MediaType.parse("application/json; charset=utf-8")

    var lastDate: String = ""

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    //Callback to adapter  interface.
    interface RefreshClick {
        fun refreshy()
    }

    //When this is called, the callback in the fragment is also called
    private var callback: RefreshClick? = null

    //on click set callback
    fun refreshyClickListener(listener: RefreshClick) {
        this.callback = listener
    }

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Shift {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {

            view = inflater.inflate(R.layout.shift_layout, parent, false)

            holder = ViewHolder() //Connect Components with holder Components
            holder.dateTextView = view.findViewById(R.id.shift_open_textView_date) as TextView
            holder.thumbnailImageView = view.findViewById(R.id.shift_user_imageButton) as ImageView
            holder.titleTextView = view.findViewById(R.id.shift_list_title) as TextView
            holder.subtitleTextView = view.findViewById(R.id.shift_list_subtitle) as TextView
            holder.requestImageButton = view.findViewById(R.id.shift_imageButton) as ImageButton

            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        var dateTextView = holder.dateTextView
        val titleTextView = holder.titleTextView
        val subtitleTextView = holder.subtitleTextView
        val thumbnailImageView = holder.thumbnailImageView
        val requestImageButton = holder.requestImageButton

        val openShift = getItem(position)
        val shift_username = openShift.user.username
        requestImageButton.tag = openShift.shift_id

        //Check to print the date above the shift or not
        if (lastDate != openShift.date) {
            lastDate = openShift.date
            dateTextView.text = lastDate
        } else {
            dateTextView.visibility = View.GONE
        }

        titleTextView.text = openShift.start + " - " + openShift.end
        subtitleTextView.text = openShift.user.name

        //Decode the picture and display it
        val imageByteArray = Base64.getDecoder().decode(openShift.user.picture)
        val bmp = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
        if (openShift.user.picture != "") {
            thumbnailImageView.setImageBitmap(bmp)
        } else {
            //If not picture, use android standard ic launcher as picture
            thumbnailImageView.setImageResource(R.mipmap.ic_launcher)
        }

        //Check which icon to show for actions
        requestImageButton.visibility = View.VISIBLE
        if (username.toLowerCase() == shift_username.toLowerCase()) {
            if (openShift._open == true) {
                //cancel button
                requestImageButton.setImageResource(R.drawable.pending)
            } else {
                //request off button
                requestImageButton.setImageResource(R.drawable.requestoff)
            }
        } else {
            if (openShift._open == true) {
                //take over
                requestImageButton.setImageResource(R.drawable.takeover)
            } else {
                requestImageButton.visibility = View.GONE
            }
        }

        val btn_user_shift = view.findViewById<ImageButton>(R.id.shift_user_imageButton)
        btn_user_shift?.setOnClickListener {
            //open profile user clicked
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra("username", shift_username)
            context.startActivity(intent)
        }

        val btn_shift = view.findViewById<ImageButton>(R.id.shift_imageButton)
        btn_shift?.setOnClickListener { view ->
            var modal = View.inflate(context, R.layout.dialog_shift, null)
            var alertDialog = AlertDialog.Builder(context)
            modal.findViewById<TextView>(R.id.dialog_date).text =
                ("      " + openShift.date + "  " + openShift.start + " - " + openShift.end)

            //change icon and askfree/or cancel
            if (username.toLowerCase() == shift_username.toLowerCase()) {
                if (openShift._open == true) {
                    //cancel button
                    alertDialog.setTitle("Cancel request")
                    alertDialog.setCancelable(false)

                    modal.findViewById<TextView>(R.id.dialog_textView).text =
                        "      Are you sure you want to cancel?"

                    alertDialog.setPositiveButton("YES") { dialog, which ->
                        // Do something when user press the positive button
                        apiCall_cancelRequest(requestImageButton.tag as Int)
                        requestImageButton.setImageResource(R.drawable.requestoff)
                        Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
                    }
                    alertDialog.setNeutralButton("NO") { dialog, which ->
                        Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    //request off button
                    alertDialog.setTitle("Request off")
                    alertDialog.setCancelable(false)

                    modal.findViewById<TextView>(R.id.dialog_textView).text =
                        "      Are you sure you want to request off?"

                    alertDialog.setPositiveButton("YES") { dialog, which ->
                        // Do something when user press the positive button
                        apiCall_askfree(requestImageButton.tag as Int)
                        requestImageButton.setImageResource(R.drawable.pending)
                        Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
                    }
                    alertDialog.setNeutralButton("NO") { dialog, which ->
                        Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                if (openShift._open == true) {
                    //take over
                    alertDialog.setTitle("Take Over")
                    alertDialog.setCancelable(false)

                    modal.findViewById<TextView>(R.id.dialog_textView).text =
                        "      Are you sure you want to fill in this shift?"

                    alertDialog.setPositiveButton("YES") { dialog, which ->
                        // Do something when user press the positive button
                        apiCall_takeover(requestImageButton.tag as Int)
                        requestImageButton.setImageResource(R.drawable.requestoff)
                        Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
                    }
                    alertDialog.setNeutralButton("NO") { dialog, which ->
                        Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    requestImageButton.visibility = View.GONE
                }


            }
            alertDialog.setView(modal)
            alertDialog.show()
        }


        return view
    }


    private class ViewHolder {
        lateinit var dateTextView: TextView
        lateinit var titleTextView: TextView
        lateinit var subtitleTextView: TextView
        lateinit var thumbnailImageView: ImageView
        lateinit var requestImageButton: ImageButton
    }

    override fun onFailure(call: Call, e: IOException) {

    }

    override fun onResponse(call: Call, response: Response) {
        if (response.isSuccessful) {
            if (response.request().url().toString() == context.resources?.getString(R.string.URL) + "/shift") {
                var getResponse = response.body()?.string()
                var shifts = Gson().fromJson(getResponse, Array<Shift>::class.java).toList()

                dataSource = shifts
            }
            callback?.refreshy()

        } else {

        }
    }

    //Apicall to update the shift to open
    fun apiCall_askfree(shift_id: Int) {
        val json = "{\"shift_id\": \"$shift_id\" }"
        val body = RequestBody.create(JSON, json)

        val request = Request.Builder()
            .url(context.resources?.getString(R.string.URL) + context.resources?.getString(R.string.URL_shift_askfree))
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("Authorization", authorization)
            .put(body)
            .build()

        client.newCall(request).enqueue(this)

    }

    //Apicall to update the user of a shift (take over an shift)
    fun apiCall_takeover(shift_id: Int) {
        val json = "{\"shift_id\": \"$shift_id\" }"
        val body = RequestBody.create(JSON, json)

        val request = Request.Builder()
            .url(context.resources?.getString(R.string.URL) + context.resources?.getString(R.string.URL_shift_takeover))
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("Authorization", authorization)
            .put(body)
            .build()

        client.newCall(request).enqueue(this)

    }

    //Apicall to cancel an request (so its not open anymore)
    fun apiCall_cancelRequest(shift_id: Int) {
        val json = "{\"shift_id\": \"$shift_id\" }"
        val body = RequestBody.create(JSON, json)

        val request = Request.Builder()
            .url(context.resources?.getString(R.string.URL) + context.resources?.getString(R.string.URL_shift_cancelRequest))
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("Authorization", authorization)
            .put(body)
            .build()

        client.newCall(request).enqueue(this)

    }

    fun apiCall() {

        val request = Request.Builder()
            .url(context.resources?.getString(R.string.URL) + context.resources?.getString(R.string.URL_shift))
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("Authorization", authorization)
            .get()
            .build()

        client.newCall(request).enqueue(this)


    }


}
