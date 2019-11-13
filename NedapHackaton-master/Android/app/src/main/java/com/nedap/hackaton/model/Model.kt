package com.nedap.hackaton.model

import android.preference.PreferenceManager
import com.google.gson.Gson
import com.nedap.hackaton.Hackaton
import com.nedap.hackaton.model.Constants.CLIENTS
import com.google.gson.reflect.TypeToken



data class Model(val app: Hackaton) {
    var clientListType = object : TypeToken<ArrayList<Client>>() {}.type
    val gson = Gson()
	val preferences = PreferenceManager.getDefaultSharedPreferences(app)
	var clients: ArrayList<Client>
		get() = gson.fromJson(preferences.getString(CLIENTS, "[]"), clientListType)
		set(value) = preferences.edit().putString(CLIENTS, gson.toJson(value, clientListType)).apply()
}
