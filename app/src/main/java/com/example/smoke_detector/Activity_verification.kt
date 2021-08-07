package com.example.smoke_detector

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.smoke_detector.R

class Activity_verification : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        val btn_back = findViewById<Button>(R.id.btn_back_verification)
        btn_back.setOnClickListener{
            finish()
        }

        val btn_next = findViewById<Button>(R.id.btn_next)
        btn_next.setOnClickListener {
            val intent = Intent(this,Activity_change::class.java)
            startActivity(intent)
        }
    }
}