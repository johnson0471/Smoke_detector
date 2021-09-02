package com.example.smoke_detector

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.smoke_detector.R
import com.google.firebase.auth.FirebaseAuth

class Activity_start : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)


        val btn_enter = findViewById<Button>(R.id.btn_enter)
        btn_enter.setOnClickListener{
            val intent = Intent(this,Activity_login::class.java)
            startActivity(intent)
            finish()
        }


    }
//    override fun onStart() {
//        auth = FirebaseAuth.getInstance()
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        val intent = Intent(this,Conrtol_Fragment_Activity::class.java)
//        if (currentUser != null )
//            startActivity(intent)
//    }
}