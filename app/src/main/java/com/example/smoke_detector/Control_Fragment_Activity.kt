package com.example.smoke_detector

import android.app.Service
import android.content.*
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.example.smoke_detector.R.layout.*
import com.example.smoke_detector.databinding.ActivityControlBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import java.util.concurrent.TimeUnit
import androidx.annotation.NonNull
import androidx.core.content.getSystemService
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.database.*
import java.security.Provider


class Control_Fragment_Activity : AppCompatActivity(), ServiceConnection {

    private lateinit var binding: ActivityControlBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private val TAG = javaClass.simpleName
    var myService: MyService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControlBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestEmail()
//            .build()
//        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val intent = Intent(this, MyService::class.java)
//        startService(intent)
        bindService(intent, this, Context.BIND_AUTO_CREATE)
        btmNavigationView()
        drawerOpen()
        navFunction()
        myWorkManager()
        cameraTesting()
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
            val currentUser = auth.currentUser
            when (it.itemId) {
                R.id.item_logout -> {
                    AlertDialog.Builder(this)
                        .setTitle("登出帳號")
                        .setMessage("確定要登出您的帳號嗎")
                        .setCancelable(false)
                        .setPositiveButton("確定") { _, _ ->
                            unbindService(this)
//                            val intent = Intent(this,MyService::class.java)
//                            stopService(intent)
                            auth.signOut()
                            database.child("已登入").child("email").removeValue()
                            database.child("已登入").child("password").removeValue()
                            startActivity(Intent(this, Activity_login::class.java))
                            finish()
                            if (currentUser != null) {
                                Log.e(TAG, currentUser.email.toString())
                            }
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

    private fun drawerOpen() {
        val materialToolbar = findViewById<MaterialToolbar>(R.id.materialToolbar)
        materialToolbar.setNavigationOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(binding.navigationDrawer)) {
                binding.drawerLayout.closeDrawer(binding.navigationDrawer)

            } else {
                binding.drawerLayout.openDrawer(binding.navigationDrawer)
            }
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
                val tpData = snapshot.child("test").child("溫度").child("5").value.toString()
                val smData = snapshot.child("test").child("煙霧").child("5").value.toString().toInt()
                val tpJudge =
                    snapshot.child("Judgment").child("temperature").value.toString().toInt()
                val tpInt = tpData.substring(0, tpData.indexOf("°C")).toInt()
                when {
                    tpInt >= tpJudge && smData == 1 -> {
                        cameraDialog()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
            }
        }
        database.addValueEventListener(dataListener)
    }

    private fun cameraDialog() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibratePhone()
        AlertDialog.Builder(this)
            .setIcon(R.drawable.ic_baseline_warning_24)
            .setTitle("火災警報")
            .setMessage("目前屋內溫度及煙霧警示異常\n請盡快開啟攝像監控並開啟鏡頭")
            .setCancelable(false)
            .setPositiveButton("確定") { _, _ ->
                vibrator.cancel()
            }
            .show()
    }

    private fun vibratePhone() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val pattern = longArrayOf(200, 2000)
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

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MyService.MainBinder
        myService = binder.getService()
        myService?.temperatureWarring()
        myService?.smokeWarring()
        myService?.residentialWarring()
        Log.d(TAG, "onServiceConnected")

    }

    override fun onServiceDisconnected(name: ComponentName?) {
        myService = null
        Log.d(TAG, "onServiceDisconnected")
    }
}


