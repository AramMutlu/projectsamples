package com.example.workr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.IOException


class ParentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)
        //Set the bottomNavigation
        val navView: BottomNavigationView = findViewById(R.id.navigation)
        val navController = findNavController(R.id.fragment)
        //Connect the BottomNav Config to the Navigation components
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_overview, R.id.navigation_openshifts, R.id.navigation_profile
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        //exitProcess(1) //Replace
    }


}
