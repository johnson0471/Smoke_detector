package com.example.smoke_detector

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.smoke_detector.R.layout.activity_registered
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.*


class Activity_registered : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_registered)
        auth = getInstance()

        val btn_registered = findViewById<Button>(R.id.btn_registered2)
        btn_registered.setOnClickListener {
            register_judgement()
        }
    }

    fun register_judgement() {

        val input_name = findViewById<TextInputLayout>(R.id.input_name_et)
        val input_email = findViewById<TextInputLayout>(R.id.input_email_rg)
        val input_password = findViewById<TextInputLayout>(R.id.input_password_rg)
        val input_agpassword = findViewById<TextInputLayout>(R.id.input_ag_password_rg)
        val et_name = findViewById<TextInputEditText>(R.id.et_name)
        val et_email = findViewById<TextInputEditText>(R.id.et_email_rg)
        val et_password = findViewById<TextInputEditText>(R.id.et_password_rg)
        val et_ag_password = findViewById<TextInputEditText>(R.id.et_ag_password_rg)



        if (et_name.text.toString().isEmpty() || et_name.text.toString().length > 10) {
            input_name.error = "請輸入您的名字，且不能超過10個字元"
        } else {
            input_name.error = null
        }

        if (et_email.text.toString().isEmpty()) {
            input_email.error = "請輸入正確的電子郵件!!!"
        } else {
            input_email.error = null
        }


        if (et_password.text.toString().isEmpty() || et_password.text.toString().length > 10) {
            input_password.error = "請輸入您的密碼，且不能超過10個字元"
        } else {
            input_password.error = null
        }

        if (et_ag_password.text.toString()
                .isEmpty() || et_ag_password.text.toString().length > 10
        ) {
            input_agpassword.error = "請再次輸入您的密碼，且不能超過10個字元"
        } else {
            input_agpassword.error = null
        }

        if (et_password.text.toString() != et_ag_password.text.toString()) {
            input_agpassword.error = "密碼必須兩者相同!!!"
            Toast.makeText(this, "註冊失敗!!!", Toast.LENGTH_LONG).show()

        } else if (et_name.text.toString().isNotEmpty() && et_email.text.toString()
                .isNotEmpty() && et_password.text.toString()
                .isNotEmpty() && et_ag_password.text.toString().isNotEmpty()
        ) {
            registedUser()
        } else {
            Toast.makeText(this, "註冊失敗!!!", Toast.LENGTH_LONG).show()
        }
    }



     fun registedUser() {
         val et_email = findViewById<TextInputEditText>(R.id.et_email_rg)
         val et_password = findViewById<TextInputEditText>(R.id.et_password_rg)
         val email = et_email.text.toString()
         val password = et_password.text.toString()
         val intent_login = Intent(this,Activity_login::class.java)
         auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                task ->
                if (task.isSuccessful){
                    Log.e("Task message","Successful")
                    Toast.makeText(this, "註冊成功!!!", Toast.LENGTH_LONG).show()
                    startActivity(intent_login)
                }else
                    Log.e("Task message","Failed"+task.exception)
                Toast.makeText(this, "這個帳戶已有使用者註冊,或密碼小於6個字", Toast.LENGTH_LONG).show()

            }
    }

    /*override fun onStart() {
        super.onStart()
        val intent_temperature = Intent(this,Activity_temperature::class.java)
        val currentUser = auth.currentUser
        val intent_login = Intent(this,Activity_login::class.java)
        if (currentUser != null){
            startActivity(intent_temperature)
        }else{
            startActivity(intent_login)
        }
    }*/


}

