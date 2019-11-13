package com.nedap.hackaton.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nedap.hackaton.R
import com.nedap.hackaton.ui.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_home, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		action_add_client.setOnClickListener {
			val action = HomeFragmentDirections.actionHomeFragmentToAddClientFragment()
			findNavController().navigate(action)
		}

		recycler_view_clients.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
		recycler_view_clients.adapter = ClientsAdapter(model.clients) { client ->
			val action = HomeFragmentDirections.actionHomeFragmentToReportsFragment(client)
			findNavController().navigate(action)
		}
	}

	override fun onResume() {
		super.onResume()
		recycler_view_clients.adapter?.notifyDataSetChanged()
	}
}
