package com.example.smoke_detector


import android.app.ActivityGroup
import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast


import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class Activity_login : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_login)

        val input_email = findViewById<TextInputLayout>(R.id.input_email_login)
        val input_password = findViewById<TextInputLayout>(R.id.input_password_login)
        val email = findViewById<TextInputEditText>(R.id.et_email_login)
        val password = findViewById<TextInputEditText>(R.id.et_password_login)
        val btn_login = findViewById<Button>(R.id.btn_login)
        val btn_register = findViewById<Button>(R.id.btn_registered)
        val intent_registered = Intent(this, Activity_registered::class.java)
        btn_login.setOnClickListener {

            if (email.text.toString().isEmpty()) {
                input_email.error = "請輸入正確的帳號"
            } else {
                input_email.error = null
            }
            if (password.text.toString().isEmpty()) {
                input_password.error = "請輸入正確的密碼"
            } else {
                input_password.error = null
            }
            if (email.text.toString().isNotEmpty() && password.text.toString().isNotEmpty()) {

                Log.e("task", "error")
                signinuser()
            }

        }

        btn_register.setOnClickListener {
            startActivity(intent_registered)
        }
    }

    fun signinuser() {


        val email = findViewById<TextInputEditText>(R.id.et_email_login)
        val password = findViewById<TextInputEditText>(R.id.et_password_login)
        val input_email = findViewById<TextInputLayout>(R.id.input_email_login)
        val input_password = findViewById<TextInputLayout>(R.id.input_password_login)
        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.e("Task message", "Successful")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.e("Task message", "signInWithEmail:failure", task.exception)
                    val user_auth = auth.currentUser
                    user_auth?.let {
                        val email_auth = user_auth.email
                        if (email_auth != email.text.toString())
                        {
                            input_email.error = "請重新輸入電子郵件"
                        }else input_email.error = null
                    }
                    input_password.error = "請重新輸入密碼"
                    updateUI(null)
                }
            }
    }

    private fun updateUI(currentuser: FirebaseUser?) {
        val intent = Intent(this,ControlActivity::class.java)
        if (currentuser != null) {
            if (currentuser.isEmailVerified){
                Toast.makeText(this, "恭喜會員登入成功~~~", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            }else
                Toast.makeText(this, "請驗證您的電子郵件", Toast.LENGTH_SHORT).show()
        }else
            Toast.makeText(this, "電子郵件或密碼有誤，請重新檢查", Toast.LENGTH_SHORT).show()

    }
}










