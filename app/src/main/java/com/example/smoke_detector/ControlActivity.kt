package com.example.smoke_detector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.smoke_detector.R.layout.activity_control
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView




class ControlActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_control)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val navController = findNavController(R.id.fragment)
        bottomNavigationView.setupWithNavController(navController)

        val materialToolbar = findViewById<MaterialToolbar>(R.id.materialToolbar)
        val title = materialToolbar.title
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.temperatureFragment,R.id.humidityFragment,R.id.smokeFragment,R.id.residentialFragment))
        materialToolbar.setupWithNavController(navController,appBarConfiguration)

    }
}