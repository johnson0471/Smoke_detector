package com.example.smoke_detector

import android.content.*
import android.net.Uri
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.example.smoke_detector.R.layout.*
import com.example.smoke_detector.databinding.ActivityControlBinding
import com.example.smoke_detector.databinding.ActivityPersonalBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import java.util.*
import java.util.concurrent.TimeUnit
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class Control_Fragment_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityControlBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControlBinding.inflate(layoutInflater)
        setContentView(binding.root)
        btmNavigationView()
        drawerOpen()
        navFunction()
        myWorkManager()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")

    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    private fun navFunction() {
        binding.navigationDrawer.setNavigationItemSelectedListener {
            auth = FirebaseAuth.getInstance()
            database = FirebaseDatabase.getInstance().reference
            when (it.itemId) {
                R.id.item_logout -> {
                    AlertDialog.Builder(this)
                        .setTitle("登出帳號")
                        .setMessage("確定要登出您的帳號嗎")
                        .setCancelable(false)
                        .setPositiveButton("確定") { _, _ ->
                            Firebase.auth.signOut()
                            database.child("已登入").child("name").removeValue()
                            database.child("已登入").child("email").removeValue()
                            database.child("已登入").child("password").removeValue()
                            startActivity(Intent(this,Activity_login::class.java))
                            finish()
                            val intent = Intent(this,MyService::class.java)
                            stopService(intent)
                        }
                        .setNegativeButton("取消", null)
                        .show()
                    binding.drawerLayout.closeDrawer(binding.navigationDrawer)
                }
                R.id.item_setting -> {
                    startActivity(Intent(this, SettingFragment::class.java))
                    finish()
                }
                R.id.item_internet->{
                    Toast.makeText(this, "開啟外部網頁", Toast.LENGTH_SHORT).show()
                    val uri = Uri.parse("https://www.nfa.gov.tw/cht/index.php?")
                    val intent = Intent(Intent.ACTION_VIEW,uri)
                    startActivity(intent)
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }




    private fun drawerOpen() {
        val materialToolbar = findViewById<MaterialToolbar>(R.id.materialToolbar)
        materialToolbar.setNavigationOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(binding.navigationDrawer)) {
                binding.drawerLayout.closeDrawer(binding.navigationDrawer)

            } else {
                binding.drawerLayout.openDrawer(binding.navigationDrawer)
            }
        }
        materialToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.account_data -> {
                    startActivity(Intent(this, PersonalActivity::class.java))
                    finish()
                    val intent = Intent(this,MyService::class.java)
                    stopService(intent)
                }
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun btmNavigationView() {
        val materialToolbar = findViewById<MaterialToolbar>(R.id.materialToolbar)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.setOnItemReselectedListener {
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.temperatureFragment -> {
                    materialToolbar.title = "溫度監控"
                }
            }

            when (destination.id) {
                R.id.smokeFragment -> {
                    materialToolbar.title = "煙霧監控"
                }
            }
            when (destination.id) {
                R.id.cameraFragment -> {
                    materialToolbar.title = "攝像監控"
                }
            }
            when (destination.id) {
                R.id.residentialFragment -> {
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
}


