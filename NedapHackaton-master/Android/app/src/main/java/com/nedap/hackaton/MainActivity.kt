package com.nedap.hackaton

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.nedap.hackaton.ui.common.BaseActivity

class MainActivity : BaseActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		val navController = findNavController(R.id.nav_host_fragment)
		val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_home))
		setupActionBarWithNavController(navController, appBarConfiguration)
	}

	override fun onSupportNavigateUp(): Boolean {
		return findNavController(R.id.nav_host_fragment).navigateUp()
	}
}
