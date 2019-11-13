package com.nedap.hackaton.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import java.text.SimpleDateFormat
import java.util.*

abstract class Utils {
	companion object {
		fun hideKeyboard(context: Activity?) {
			context?.let {
				val view = context.currentFocus
				view?.let {
					val inputMethodManager = context
						.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
					inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
				}
			}
		}

		fun readableDate(date: Date): String {
			val calendar = Calendar.getInstance().apply {
				this.time = date
			}

			return "${calendar.get(Calendar.DATE)} ${calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())} ${calendar.get(Calendar.YEAR)}"
		}

		fun getColorBloodPressure(high: Int, low: Int): Color {
			when (high){
				in 0..89 ->{
					when (low){
						in 0..59 ->{
							return Color.ORANGE
						}
						in 60..79 ->{
							return Color.GREEN
						}
						in 80..89 ->{
							return Color.ORANGE
						}
						!in 40..89 ->{
							return Color.RED
						}
					}
				}
				in 90..119 -> {
					when (low){
						in 40..79 ->{
							return Color.GREEN
						}
						in 80..89 ->{
							return Color.ORANGE
						}
						!in 40..89 ->{
							return Color.RED
						}
					}
				}
				in 120..139 -> {
					when (low){
						in 40..89 ->{
							return Color.ORANGE
						}
						!in 40..89 ->{
							return Color.RED
						}
					}
				}
				!in 0..140 ->{
					return Color.RED
				}
			}
			return Color.RED
		}
		enum class Color {
			GREEN, ORANGE, RED
		}
	}
}
