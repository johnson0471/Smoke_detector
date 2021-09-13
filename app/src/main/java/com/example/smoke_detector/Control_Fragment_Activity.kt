package com.example.smoke_detector

import android.animation.ObjectAnimator
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.smoke_detector.R.layout.activity_control
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_temperature.*


class Control_Fragment_Activity : AppCompatActivity() {


    lateinit var toolbar: MaterialToolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    private lateinit var auth: FirebaseAuth
    private val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_control)
        drawerLayout = findViewById(R.id.drawerLayout)
        toolbar = findViewById(R.id.materialToolbar)
        navigationView = findViewById(R.id.navigation_drawer)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)



        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.temperatureFragment -> {
                    toolbar.title = "溫度監控"
                }
            }
            when (destination.id) {
                R.id.humidityFragment -> {
                    toolbar.title = "濕度監控"
                }
            }
            when (destination.id) {
                R.id.smokeFragment -> {
                    toolbar.title = "煙霧監控"
                }
            }
            when (destination.id) {
                R.id.cameraFragment -> {
                    toolbar.title = "居家安全"
                }
            }
        }



        toolbar.setNavigationOnClickListener {

            if (drawerLayout.isDrawerOpen(navigationView)) {
                drawerLayout.closeDrawer(navigationView)
                toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24)

            } else {
                drawerLayout.openDrawer(navigationView)
                toolbar.setNavigationIcon(R.drawable.ic_arrow)
            }
        }

        navigationView.setNavigationItemSelectedListener {
            auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            Log.e(TAG, currentUser.toString())
            when (it.itemId) {
                R.id.item_logout -> {

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
            }
            return@setNavigationItemSelectedListener true
        }


    }
}


