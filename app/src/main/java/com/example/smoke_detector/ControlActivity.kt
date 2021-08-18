package com.example.smoke_detector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.findFragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.smoke_detector.R.id.fragmentContainerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ControlActivity : AppCompatActivity() {

    companion object{
        val temperatureFragment = TemperatureFragment()
        val humidityFragment = HumidityFragment()
        val smokeFragment = SmokeFragment()
        val residentialFragment = ResidentialFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val nav_control = findNavController(fragmentContainerView)

        bottomNavigationView.setupWithNavController(nav_control)

    }
}