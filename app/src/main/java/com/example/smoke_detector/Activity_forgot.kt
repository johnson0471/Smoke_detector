package com.example.smoke_detector

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.smoke_detector.R

class Activity_forgot : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)
        val intent = Intent(this,Activity_verification::class.java)
        val btn_determine = findViewById<Button>(R.id.btn_determine)

        btn_determine.setOnClickListener {
            startActivity(intent)
        }



    }
}