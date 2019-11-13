package com.nedap.hackaton

import android.app.Application
import com.facebook.stetho.Stetho
import com.nedap.hackaton.model.Client
import com.nedap.hackaton.model.Constants
import com.nedap.hackaton.model.Constants.CLIENTS
import com.nedap.hackaton.model.Model
import com.nedap.hackaton.model.Report

class Hackaton : Application() {
	lateinit var model: Model

	override fun onCreate() {
		super.onCreate()
		model = Model(this)

		if (model.preferences.getString(CLIENTS, null) == null) {
			model.clients = arrayListOf(
				Client().apply {
					this.name = "Truus "
					this.reports = arrayListOf(
						Report().apply {
							this.text = "Test"
							this.measurements[Constants.BLOOD] = "110/60"
							this.measurements[Constants.BODY_TEMPERATURE] = 40.7
							this.measurements[Constants.WEIGHT] = 90.4
						},
						Report().apply {
							this.text = "Test"
							this.measurements[Constants.BLOOD] = "115/55"
							this.measurements[Constants.BODY_TEMPERATURE] = 36.7
							this.measurements[Constants.WEIGHT] = 87.4
						},
						Report().apply {
							this.text = "Test"
							this.measurements[Constants.BLOOD] = "120/80"
							this.measurements[Constants.BODY_TEMPERATURE] = 23.7
							this.measurements[Constants.WEIGHT] = 88.4
						}
					)
				},
				Client().apply {
					this.name = "Bertha Cornelissen"
					this.reports = arrayListOf(
						Report().apply {
							this.text = "Test"
							this.measurements[Constants.BLOOD] = "170/100"
							this.measurements[Constants.BODY_TEMPERATURE] = 34.9
							this.measurements[Constants.WEIGHT] = 82.6
						}
					)
				}
			)
		}

		Stetho.initializeWithDefaults(this)
	}
}