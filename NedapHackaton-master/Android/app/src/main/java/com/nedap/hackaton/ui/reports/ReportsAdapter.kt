package com.nedap.hackaton.ui.reports

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.nedap.hackaton.R
import com.nedap.hackaton.model.Client
import com.nedap.hackaton.model.Constants.BLOOD
import com.nedap.hackaton.model.Constants.BODY_TEMPERATURE
import com.nedap.hackaton.model.Constants.WEIGHT
import com.nedap.hackaton.model.Report
import com.nedap.hackaton.utils.Utils
import kotlinx.android.synthetic.main.item_client.view.*

class ReportsAdapter(var reports: List<Report>, var onClick: (Report) -> Unit ) :
    RecyclerView.Adapter<ReportsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_report, parent, false))

    override fun getItemCount(): Int = reports.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        reports[holder.adapterPosition].let { report ->
			holder.title.text = Utils.readableDate(report.date)
			holder.subtitle.text = "${report.measurements[BLOOD].toString()}  |  ${report.measurements[BODY_TEMPERATURE].toString()}°C  |  ${report.measurements[WEIGHT].toString()}kg"
        }

        holder.itemView.setOnClickListener {
            onClick(reports[holder.adapterPosition])
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: AppCompatTextView = view.title
        val subtitle: AppCompatTextView = view.subtitle
    }
}