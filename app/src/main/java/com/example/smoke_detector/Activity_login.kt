package com.example.smoke_detector


import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class Activity_login : AppCompatActivity() {

    companion object{
        private const val RC_SIGN_IN = 120
    }


    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_login)

        val btn_forgot_password = findViewById<TextView>(R.id.btn_forgot_password)
        val input_email = findViewById<TextInputLayout>(R.id.input_email_login)
        val input_password = findViewById<TextInputLayout>(R.id.input_password_login)
        val email = findViewById<TextInputEditText>(R.id.et_email_login)
        val password = findViewById<TextInputEditText>(R.id.et_password_login)
        val btn_login = findViewById<Button>(R.id.btn_login)
        val btn_google = findViewById<Button>(R.id.btn_google)
        val btn_register = findViewById<Button>(R.id.btn_registered)
        val intent_registered = Intent(this, Activity_registered::class.java)
        val intent_forgot = Intent(this, Activity_forgot::class.java)

        btn_login.setOnClickListener {

            when {
                email.text.toString().isEmpty() -> {
                    input_email.error = "請輸入正確的電子郵件"
                }
                !Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches() -> {
                    input_email.error = "電子郵件輸入格式有誤，請再輸入一次"
                }
                else -> input_email.error = null
            }

            when {
                password.text.toString().isEmpty() -> {
                    input_password.error = "請輸入正確的密碼"
                }
                password.text.toString().length < 6 -> {
                    input_password.error = "密碼不得小於6字，請再輸入一次"
                }
                else -> input_password.error = null
            }

            if (email.text.toString().isNotEmpty() && password.text.toString().isNotEmpty()) {
                signinuser()
            }

        }

        btn_register.setOnClickListener {
            startActivity(intent_registered)
        }
        btn_forgot_password.setOnClickListener {
            startActivity(intent_forgot)
        }

        btn_google.setOnClickListener{
            signIn()
        }
    }

    private fun signinuser() {

        val email = findViewById<TextInputEditText>(R.id.et_email_login)
        val password = findViewById<TextInputEditText>(R.id.et_password_login)
        val input_email = findViewById<TextInputLayout>(R.id.input_email_login)
        val input_password = findViewById<TextInputLayout>(R.id.input_password_login)
        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.e("Task message", "Successful")
                    input_email.error = null
                    input_password.error = null
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.e("Task message", "signInWithEmail:failure", task.exception)
                    val user_auth = auth.currentUser
                    user_auth.let {
                        val email_auth = user_auth?.email
                        if (email_auth != email.text.toString()) {
                            input_email.error = "請重新輸入電子郵件"
                        } else input_email.error = null
                    }
                    input_password.error = "請重新輸入密碼"
                    updateUI(null)
                }
            }
    }

    private fun updateUI(currentuser: FirebaseUser?) {
        val intent = Intent(this, Conrtol_Fragment_Activity::class.java)
        if (currentuser != null) {
            if (currentuser.isEmailVerified) {
                Toast.makeText(this, "恭喜會員登入成功~~~", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            } else
                Toast.makeText(this, "請驗證您的電子郵件", Toast.LENGTH_SHORT).show()
        } else
            Toast.makeText(this, "電子郵件或密碼有誤，請重新檢查", Toast.LENGTH_SHORT).show()

    }

    private fun googleSignIn(){
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

}










