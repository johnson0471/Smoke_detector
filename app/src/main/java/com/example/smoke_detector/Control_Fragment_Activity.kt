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
        startService(Intent(this, MyService::class.java))
        btmNavigationView()
        drawerOpen()
        navFunction()
        myWorkManager()
        cameraTesting()
        internet()
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
                            startActivity(Intent(this, Activity_login::class.java))
                            finish()
                            val intent = Intent(this, MyService::class.java)
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
            }
            return@setNavigationItemSelectedListener true
        }
    }

    private fun internet() {
        val header = binding.navigationDrawer.inflateHeaderView(R.layout.header_navigation)
        val imageButton = header.findViewById<ImageButton>(R.id.fireStation)
        imageButton.setOnClickListener {
            Toast.makeText(this, "開啟外部網頁", Toast.LENGTH_SHORT).show()
            val uri = Uri.parse("https://www.nfa.gov.tw/cht/index.php?")
            val intent = Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)
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

//    private fun signOut() {
//        mGoogleSignInClient.signOut()
//            .addOnCompleteListener(this, OnCompleteListener<Void?> {
//                // ...
//            })
//    }

    private fun cameraTesting() {
        database = FirebaseDatabase.getInstance().reference
        val dataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tpData = snapshot.child("溫度").child("5").value.toString()
                val smData = snapshot.child("煙霧").child("5").value.toString().toInt()
                database.child("Judgment").child("temperature").get().addOnSuccessListener {
                    val tpJudge = it.value.toString().toInt()
                    val tpInt = tpData.substring(0, tpData.indexOf("°C")).toInt()
                    when {
                        tpInt >= tpJudge && smData == 1 -> {
                            if (!isFinishing) {
                                cameraDialog()
                            }
                        }
                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
            }
        }
        database.child("test").addValueEventListener(dataListener)
    }

    private fun cameraDialog() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibratePhone()
        AlertDialog.Builder(this)
            .setIcon(R.drawable.ic_baseline_warning_24)
            .setTitle("火災警報")
            .setMessage("目前屋內溫度及煙霧警示異常\n請盡快開啟攝像監控並開啟鏡頭")
            .setCancelable(false)
            .setPositiveButton("確定") { dialog, _ ->
                vibrator.cancel()
                dialog.dismiss()
            }
            .show()
    }

    private fun vibratePhone() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val pattern = longArrayOf(200, 1000)
        vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0))
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


