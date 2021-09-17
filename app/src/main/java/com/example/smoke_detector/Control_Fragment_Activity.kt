package com.example.smoke_detector

import android.animation.ObjectAnimator
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBarDrawerToggle

import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.fragment.NavHostFragment

import androidx.navigation.ui.setupWithNavController
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.request.transition.Transition
import com.example.smoke_detector.R.layout.activity_control
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_control.*
import kotlinx.android.synthetic.main.app_bar_main.*


class Control_Fragment_Activity : AppCompatActivity() {


    lateinit var navigationView: NavigationView
    private lateinit var auth: FirebaseAuth
    private val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_control)
        navigationView = findViewById(R.id.navigation_drawer)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setupWithNavController(navController)

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



        materialToolbar.setNavigationOnClickListener {

            if (drawerLayout.isDrawerOpen(navigationView)) {
                drawerLayout.closeDrawer(navigationView)

            } else {
                drawerLayout.openDrawer(navigationView)
            }
        }

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
}


