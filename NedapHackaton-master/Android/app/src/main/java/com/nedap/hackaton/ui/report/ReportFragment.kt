package com.nedap.hackaton.ui.report

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.nedap.hackaton.R
import com.nedap.hackaton.model.Constants.BLOOD
import com.nedap.hackaton.model.Constants.BODY_TEMPERATURE
import com.nedap.hackaton.model.Constants.WEIGHT
import com.nedap.hackaton.model.Report
import com.nedap.hackaton.ui.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_report.*
import kotlinx.android.synthetic.main.measurements_bottom_sheet.*
import kotlinx.android.synthetic.main.measurements_bottom_sheet.view.*
import net.cachapa.expandablelayout.ExpandableLayout

class ReportFragment : BaseFragment() {
	val args: ReportFragmentArgs by navArgs()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		setHasOptionsMenu(true)
		return inflater.inflate(R.layout.fragment_report, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		if (args.report != null) {
			args.report?.let { report ->
				// Read existing report
				input_weight.setText(report.measurements[WEIGHT].toString())
				input_body_temperature.setText(report.measurements[BODY_TEMPERATURE].toString())
				input_blood.setText(report.measurements[BLOOD].toString())
				input_report.setText(report.text)

				weight_value.text = "${report.measurements[WEIGHT].toString()}kg"
				body_temp_value.text = "${report.measurements[BODY_TEMPERATURE].toString()}°C"
				blood_value.text = report.measurements[BLOOD].toString()
			}
		}

		input_weight.doAfterTextChanged {
			weight_value.text = "${input_weight.text.toString()}kg"
		}

		input_body_temperature.doAfterTextChanged {
			body_temp_value.text = "${input_body_temperature.text.toString()}°C"
		}

		input_blood.doAfterTextChanged {
			blood_value.text = input_blood.text.toString()
		}

		val expandableLayoutBodyTemperature = view.findViewById<ExpandableLayout>(R.id.expandable_layout_body_temperature)
		view.card_body_temperature.setOnClickListener {
			if (expandableLayoutBodyTemperature.isExpanded) {
				expandableLayoutBodyTemperature.collapse()
				body_temperature_chevron.rotation = 180F
			} else {
				expandableLayoutBodyTemperature.expand()
				body_temperature_chevron.rotation = 0F
			}
		}


		view.recycler_view_body.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
		recycler_view_body.adapter = MeasurementAdapter(args.client.reports, BODY_TEMPERATURE, context!!)

		val expandableLayoutBlood = view.findViewById<ExpandableLayout>(R.id.expandable_layout_blood)
		view.card_blood.setOnClickListener {
			if (expandableLayoutBlood.isExpanded) {
				expandableLayoutBlood.collapse()
				blood_chevron.rotation = 180F
			} else {
				expandableLayoutBlood.expand()
				blood_chevron.rotation = 0F
			}
		}
		view.recycler_view_blood.layoutManager =
			LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
		recycler_view_blood.adapter = MeasurementAdapter(args.client.reports, BLOOD, context!!)


		val expandableLayoutWeight = view.findViewById<ExpandableLayout>(R.id.expandable_layout_weight)
		view.card_weight.setOnClickListener {
			if (expandableLayoutWeight.isExpanded) {
				expandableLayoutWeight.collapse()
				weight_chevron.rotation = 180F
			} else {
				expandableLayoutWeight.expand()
				weight_chevron.rotation = 0F
			}
		}
		view.recycler_view_weight.layoutManager =
			LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
		recycler_view_weight.adapter = MeasurementAdapter(args.client.reports, WEIGHT, context!!)

		val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetParent)
		bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
			override fun onSlide(p0: View, p1: Float) {
				//
			}

			override fun onStateChanged(p0: View, p1: Int) {
				println(p1)

				if (p1 == BottomSheetBehavior.STATE_EXPANDED) {
					activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
					action_show_measurements.rotation = 180F
					measurements_title.visibility = View.VISIBLE
					blood_title.visibility = View.INVISIBLE
					blood_value.visibility = View.INVISIBLE
					body_temp_title.visibility = View.INVISIBLE
					body_temp_value.visibility = View.INVISIBLE
					weight_title.visibility = View.INVISIBLE
					weight_value.visibility = View.INVISIBLE
				} else {
					activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
					action_show_measurements.rotation = 0F
					measurements_title.visibility = View.GONE
					blood_title.visibility = View.VISIBLE
					blood_value.visibility = View.VISIBLE
					body_temp_title.visibility = View.VISIBLE
					body_temp_value.visibility = View.VISIBLE
					weight_title.visibility = View.VISIBLE
					weight_value.visibility = View.VISIBLE
				}
			}
		})

		action_show_measurements.setOnClickListener {
			val bottomSheet = BottomSheetBehavior.from(bottomSheetParent)

			if (bottomSheet.state == BottomSheetBehavior.STATE_COLLAPSED) {
				bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
			} else {
				bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
			}
		}
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		inflater.inflate(R.menu.menu_report, menu)
		super.onCreateOptionsMenu(menu, inflater)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if (item.itemId == R.id.action_save) {
			if (args.report != null) {
				// Edit existing report
				model.clients.find { it.id == args.client.id }?.reports?.find { it.id == args.report?.id }?.apply {
					this.text = input_report.text.toString()
					val weight = input_weight.text.toString()
					val blood = input_blood.text.toString()
					val bodyTemperature = input_body_temperature.text.toString()

					if (weight.isEmpty()) {
						Toast.makeText(context, "Please fill in weight!", Toast.LENGTH_SHORT).show()
						return super.onOptionsItemSelected(item)
					}
					if (blood.isEmpty()) {
						Toast.makeText(
							context,
							"Please fill in blood pressure!",
							Toast.LENGTH_SHORT
						).show()
						return super.onOptionsItemSelected(item)
					}
					if (bodyTemperature.isEmpty()) {
						Toast.makeText(
							context,
							"Please fill in body temperature!",
							Toast.LENGTH_SHORT
						).show()
						return super.onOptionsItemSelected(item)
					}

					this.measurements = hashMapOf(
						WEIGHT to weight,
						BLOOD to blood,
						BODY_TEMPERATURE to bodyTemperature
					)
				}
			} else {
				// Save new report
				val report = Report().apply {
					this.text = input_report.text.toString()
					val weight = input_weight.text.toString()
					val blood = input_blood.text.toString()
					val bodyTemperature = input_body_temperature.text.toString()

					if (weight.isEmpty()) {
						Toast.makeText(context, "Please fill in weight!", Toast.LENGTH_SHORT).show()
						return super.onOptionsItemSelected(item)
					}
					if (blood.isEmpty()) {
						Toast.makeText(
							context,
							"Please fill in blood pressure!",
							Toast.LENGTH_SHORT
						).show()
						return super.onOptionsItemSelected(item)
					}
					if (bodyTemperature.isEmpty()) {
						Toast.makeText(
							context,
							"Please fill in body temperature!",
							Toast.LENGTH_SHORT
						).show()
						return super.onOptionsItemSelected(item)
					}

					this.measurements = hashMapOf(
						WEIGHT to weight,
						BLOOD to blood,
						BODY_TEMPERATURE to bodyTemperature
					)
				}

				val clients = model.clients
				clients.find { it.id == args.client.id }?.reports?.add(report)
				model.clients = clients
			}
			findNavController().navigateUp()
		}
		return super.onOptionsItemSelected(item)
	}
}