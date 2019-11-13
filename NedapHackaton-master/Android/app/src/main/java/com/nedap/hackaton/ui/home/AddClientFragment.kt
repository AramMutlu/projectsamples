package com.nedap.hackaton.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.nedap.hackaton.R
import com.nedap.hackaton.model.Client
import com.nedap.hackaton.ui.common.BaseFragment
import com.nedap.hackaton.utils.Utils
import kotlinx.android.synthetic.main.fragment_add_client.*

class AddClientFragment : BaseFragment() {
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_add_client, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		action_save_client.setOnClickListener {
			val clients = model.clients
			clients.add(Client().apply { this.name = input_name.text.toString() })
			model.clients = clients
			Utils.hideKeyboard(activity)
			val action = AddClientFragmentDirections.actionAddClientFragmentToHomeFragment()
			findNavController().navigate(action)
		}
	}
}
