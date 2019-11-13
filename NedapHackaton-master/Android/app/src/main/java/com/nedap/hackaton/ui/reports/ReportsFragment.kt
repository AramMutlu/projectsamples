package com.nedap.hackaton.ui.reports

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.nedap.hackaton.MainActivity
import com.nedap.hackaton.R
import com.nedap.hackaton.model.Constants.BLOOD
import com.nedap.hackaton.model.Constants.BODY_TEMPERATURE
import com.nedap.hackaton.model.Constants.WEIGHT
import com.nedap.hackaton.model.Report
import com.nedap.hackaton.ui.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_reports.*
import java.util.*
import kotlin.concurrent.schedule

class ReportsFragment : BaseFragment() {
	val args: ReportsFragmentArgs by navArgs()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_reports, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		//(activity as? MainActivity)?.setToolbarTitle(args.client.name)
		recycler_view_reports.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
		recycler_view_reports.adapter = ReportsAdapter(model.clients.find { it.id == args.client.id }?.reports ?: arrayListOf()) {
			val action = ReportsFragmentDirections.actionReportsFragmentToReportFragment(args.client, it)
			findNavController().navigate(action)
		}

		action_add_report.setOnClickListener {
			val action = ReportsFragmentDirections.actionReportsFragmentToReportFragment(args.client, null)
			findNavController().navigate(action)
		}
	}

	override fun onResume() {
		super.onResume()
		recycler_view_reports.adapter?.notifyDataSetChanged()
	}
}
