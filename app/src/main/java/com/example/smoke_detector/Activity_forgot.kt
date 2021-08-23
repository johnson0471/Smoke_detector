package com.example.smoke_detector

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Activity_forgot : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)
        val intent = Intent(this, Activity_verification::class.java)
        val btn_determine = findViewById<Button>(R.id.btn_determine)

        btn_determine.setOnClickListener {
            sendPasswordEmail()
        }


    }

    fun sendPasswordEmail() {
        val input_email = findViewById<TextInputLayout>(R.id.input_email_fr)
        val et_email = findViewById<TextInputEditText>(R.id.et_email_fr)
        val emailAddress = et_email.text.toString()

        if (emailAddress.isEmpty()) {
            input_email.error = "電子郵件不可為空"
            Toast.makeText(this, "請輸入您正確的電子郵件!!!", Toast.LENGTH_SHORT).show()
        } else if (emailAddress.isNotEmpty()) {
            if (Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                input_email.error = null
                Firebase.auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.e("Task", "Successful")
                            Toast.makeText(this, "驗證郵件已成功寄出!!!", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.e("Task", "Error" + task.exception)
                            Toast.makeText(this, "此用戶尚未註冊!!!", Toast.LENGTH_SHORT).show()
                        }
                    }
            }else  input_email.error = "輸入格式有誤，請再次確認您的電子郵件"

        }
    }
}