package com.example.smoke_detector

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.example.smoke_detector.databinding.ActivityStartBinding
import com.google.firebase.auth.FirebaseAuth

class Activity_start : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding
    private lateinit var auth: FirebaseAuth
    private val TAG = javaClass.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEnter.setOnClickListener{
            val intent = Intent(this,Activity_login::class.java)
            startActivity(intent)
            finish()
        }

    }
    override fun onStart() {
        auth = FirebaseAuth.getInstance()
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        val intent = Intent(this,Control_Fragment_Activity::class.java)
        if (currentUser != null ){
            startActivity(intent)
            finish()
        }
    }
}