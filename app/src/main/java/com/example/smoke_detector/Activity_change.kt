package com.example.smoke_detector

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.smoke_detector.R
import com.google.firebase.ktx.Firebase

class Activity_change : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change)

        val btn_determine = findViewById<Button>(R.id.btn_determine)
        btn_determine.setOnClickListener {
            val intent = Intent(this,Activity_login::class.java)
            startActivity(intent)
        }

    }
}