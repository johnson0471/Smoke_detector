package com.example.smoke_detector

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.smoke_detector.R

class Activity_start : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val ber = findViewById<Button>(R.id.btn_enter)
        ber.setOnClickListener{
            val intent = Intent(this,Activity_login::class.java)
            startActivity(intent)
        }
    }
}