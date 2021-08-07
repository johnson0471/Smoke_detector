package com.example.smoke_detector

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.smoke_detector.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Activity_registered : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registered)

        val btn_registered = findViewById<Button>(R.id.btn_registered2)
        val input_name = findViewById<TextInputLayout>(R.id.input_name_et)
        val input_email = findViewById<TextInputLayout>(R.id.input_email_et)
        val input_account = findViewById<TextInputLayout>(R.id.input_account_et2)
        val input_password = findViewById<TextInputLayout>(R.id.input_password_et2)
        val input_agpassword = findViewById<TextInputLayout>(R.id.input_ag_password_et)
        val et_name =findViewById<TextInputEditText>(R.id.et_name)

        val intent = Intent(this,Activity_login::class.java)
        btn_registered.setOnClickListener{
            startActivity(intent)
        }
    }

}

