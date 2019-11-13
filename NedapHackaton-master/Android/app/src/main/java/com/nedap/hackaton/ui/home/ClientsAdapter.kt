package com.nedap.hackaton.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.nedap.hackaton.R
import com.nedap.hackaton.model.Client
import com.nedap.hackaton.utils.Utils
import kotlinx.android.synthetic.main.item_client.view.*

class ClientsAdapter(var clients: ArrayList<Client>, var onClick: (Client) -> Unit) :
	RecyclerView.Adapter<ClientsAdapter.ViewHolder>() {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
		ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_client, parent, false))

	override fun getItemCount(): Int = clients.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		clients[holder.adapterPosition].let { client ->
            val sortedClients = client.reports.sortedBy { it.date }
			holder.title.text = client.name
			holder.subtitle.text = if (sortedClients.isNotEmpty()) Utils.readableDate(sortedClients.first().date) else "No reports found"
		}

		holder.itemView.setOnClickListener {
			onClick(clients[holder.adapterPosition])
		}
	}

	class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		val title: AppCompatTextView = view.title
		val subtitle: AppCompatTextView = view.subtitle
	}
}