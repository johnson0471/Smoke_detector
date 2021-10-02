package com.example.smoke_detector

import android.animation.ObjectAnimator
import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log

import androidx.appcompat.app.AlertDialog
import androidx.core.view.forEach
import androidx.navigation.fragment.NavHostFragment

import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.example.smoke_detector.R.layout.activity_control
import com.example.smoke_detector.databinding.FragmentTemperatureBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_control.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.contain_layout.*
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit


class Control_Fragment_Activity : AppCompatActivity() {


    private lateinit var database: DatabaseReference
    private lateinit var navigationView: NavigationView
    private lateinit var auth: FirebaseAuth
    private val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_control)
        database = Firebase.database.reference
        navigationView = navigation_drawer
        myWorkManager()
        btmNavigationView()
        drawerOpen()
        navFunction()
    }

    private fun navFunction() {
        navigationView.setNavigationItemSelectedListener {
            auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            when (it.itemId) {
                R.id.item_logout -> {
                    Log.e(TAG, currentUser.toString())
                    AlertDialog.Builder(this)
                        .setTitle("登出帳號")
                        .setMessage("確定要登出您的帳號嗎")
                        .setCancelable(false)
                        .setPositiveButton("確定") { dialog, which ->
                            Firebase.auth.signOut()
                            startActivity(Intent(this, Activity_login::class.java))
                            Log.e(TAG, currentUser.toString())
                        }
                        .setNeutralButton("取消", null)
                        .show()
                    drawerLayout.closeDrawer(navigationView)
                }
                R.id.item_setting -> {
                    startActivity(Intent(this, SettingFragment::class.java))
                    finish()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    private fun drawerOpen() {
        materialToolbar.setNavigationOnClickListener {
            if (drawerLayout.isDrawerOpen(navigationView)) {
                drawerLayout.closeDrawer(navigationView)

            } else {
                drawerLayout.openDrawer(navigationView)
            }
        }
    }

    private fun btmNavigationView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.setOnItemReselectedListener {
            if (it.isChecked){
                return@setOnItemReselectedListener
            }
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.temperatureFragment -> {
                    materialToolbar.title = "溫度監控"
                }
            }
            when (destination.id) {
                R.id.humidityFragment -> {
                    materialToolbar.title = "濕度監控"
                }
            }
            when (destination.id) {
                R.id.smokeFragment -> {
                    materialToolbar.title = "煙霧監控"

                }
            }
            when (destination.id) {
                R.id.cameraFragment -> {
                    materialToolbar.title = "居家安全"
                }
            }
        }
    }



    private fun myWorkManager() {
        val constraints = Constraints.Builder()
            .setRequiresCharging(false)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val myRequest = PeriodicWorkRequest.Builder(MyWorker::class.java, 15, TimeUnit.MINUTES)
            .setConstraints(constraints).build()
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork("work_name", ExistingPeriodicWorkPolicy.KEEP, myRequest)
    }

    override fun onResume() {
        super.onResume()
        setWarning()
    }

    private fun setWarning() {
        database.child("Judgment").child("temperature").get().addOnSuccessListener {
            val tp_jg = it.value.toString().toInt()
            database.child("test").child("溫度").child("5").get().addOnSuccessListener { it1 ->
                val t = it1.value.toString()
                val tp_current = t.substring(0, t.indexOf("°C")).toInt()
                database.child("test").child("煙霧").child("5").get().addOnSuccessListener { it2 ->
                    val s = it2.value.toString().toInt()
                    if (tp_jg < tp_current && s == 1) {
                        AlertDialog.Builder(this)
                            .setTitle("開啟遠端鏡頭")
                            .setMessage("請問要開啟遠端鏡頭嗎")
                            .setCancelable(false)
                            .setPositiveButton("確定") { dialog, which ->
                            }
                            .setNeutralButton("取消", null)
                            .show()
                    } else Log.e(TAG, "Error")
                }
            }
        }
    }
}


