package com.nedap.hackaton.ui.report

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nedap.hackaton.R
import com.nedap.hackaton.model.Client
import com.nedap.hackaton.model.Constants
import com.nedap.hackaton.model.Constants.BLOOD
import com.nedap.hackaton.model.Constants.BODY_TEMPERATURE
import com.nedap.hackaton.model.Constants.WEIGHT
import com.nedap.hackaton.model.Report
import com.nedap.hackaton.utils.Utils
import kotlinx.android.synthetic.main.item_measurement.view.*

class MeasurementAdapter(var reports: List<Report>, var type: String, var context: Context) :
    RecyclerView.Adapter<MeasurementAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_measurement,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = reports.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        reports[holder.adapterPosition].let { report ->
            holder.date.text = Utils.readableDate(report.date)
            holder.value.text = "${report.measurements[type].toString()}"

            if (type == WEIGHT || type == BODY_TEMPERATURE) {
                val previousReport = reports.getOrNull(holder.adapterPosition + 1)
                if (previousReport == null) {
                    holder.difference.text = "-"
                } else {
                    val difference = ((report.measurements[type].toString().toDouble() * 100).toInt() - (previousReport.measurements[type].toString().toDouble() * 100).toInt()).toDouble() / 100
                    if (difference >= 0) {
                        holder.difference.text = "+$difference"
                        holder.difference.setTextColor(ContextCompat.getColor(context, R.color.colorUp))
                    }
                    else {
                        holder.difference.text = "$difference"
                        holder.difference.setTextColor(ContextCompat.getColor(context, R.color.colorDown))
                    }
                }
            } else if (type == BLOOD) {
                val pressure = report.measurements[type].toString().split("/")
                val high = pressure[0].trim().toInt()
                val low = pressure[1].trim().toInt()

                when(Utils.getColorBloodPressure(high, low)) {
                    Utils.Companion.Color.GREEN -> { holder.value.setTextColor(ContextCompat.getColor(context, R.color.colorUp)) }
                    Utils.Companion.Color.ORANGE -> { holder.value.setTextColor(ContextCompat.getColor(context, R.color.colorOrange)) }
                    Utils.Companion.Color.RED -> { holder.value.setTextColor(ContextCompat.getColor(context, R.color.colorDown)) }
                }
                holder.difference.text = "-"
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val date: AppCompatTextView = view.date
        val value: AppCompatTextView = view.value
        val difference: AppCompatTextView = view.difference
    }
}