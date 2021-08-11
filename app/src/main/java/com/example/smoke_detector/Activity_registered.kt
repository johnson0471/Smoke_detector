package com.example.smoke_detector

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import com.example.smoke_detector.R.layout.activity_registered
import com.example.smoke_detector.databinding.ActivityRegisteredBinding
import com.example.smoke_detector.databinding.ActivityRegisteredBinding.*
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.FirebaseDatabase.getInstance
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class Activity_registered : AppCompatActivity() {

    private lateinit var binding: ActivityRegisteredBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //auth = FirebaseAuth.getInstance()
        binding = inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegistered2.setOnClickListener{
           register_User()

        }

    }

    fun register_User() {

        val input_name = binding.inputNameRg
        val input_account = binding.inputAccountRg
        val input_email = binding.inputEmailRg
        val input_password = binding.inputPasswordRg
        val input_agpassword = binding.inputAgPasswordRg

        val username = binding.etNameRg.text.toString()
        val email = binding.etEmailRg.text.toString()
        val account = binding.etAccountRg.text.toString()
        val password = binding.etPasswordRg.text.toString()
        val ag_password = binding.etAgPasswordRg.text.toString()
        val intent_smoke = Intent(this,Activity_temperature::class.java)

        if (username.isEmpty() || username.length > 10) {
            input_name.error = "請輸入您的名字，且不能超過10個字元"
        } else {
            input_name.error = null
        }

        if (email.isEmpty()) {
            input_email.error = "請輸入正確的電子郵件!!!"
        } else {
            input_email.error = null
        }

        if (account.isEmpty() || account.length > 10) {
            input_account.error = "請輸入您的密碼，且不能超過10個字元"
        } else {
            input_account.error = null
        }


        if (password.isEmpty() || password.length > 10) {
            input_password.error = "請輸入您的密碼，且不能超過10個字元"
        } else {
            input_password.error = null
        }

        if (ag_password
                .isEmpty() || ag_password.length > 10
        ) {
            input_agpassword.error = "請再次輸入您的密碼，且不能超過10個字元"
        } else {
            input_agpassword.error = null
        }

        if (password != ag_password) {
            input_agpassword.error = "密碼必須兩者相同!!!"
            Toast.makeText(this, "註冊失敗!!!", Toast.LENGTH_LONG).show()

        } else if (username.isNotEmpty() && email
                .isNotEmpty() && account.isNotEmpty() && password
                .isNotEmpty() && ag_password.isNotEmpty())
        {
            sendUsers()
            startActivity(intent_smoke)
        } else {
            Toast.makeText(this, "註冊失敗!!!", Toast.LENGTH_LONG).show()
        }
    }

    fun sendUsers()
    {
        val username = binding.etNameRg.text.toString()
        val email = binding.etEmailRg.text.toString()
        val account = binding.etAccountRg.text.toString()


        database = Firebase.database.reference
        val User = user(username, email, account)
        database.child("Users").setValue(User).addOnSuccessListener{

            Toast.makeText(this,"使用者註冊成功",Toast.LENGTH_SHORT).show()
            binding.etNameRg.text?.clear()
            binding.etEmailRg.text?.clear()
            binding.etAccountRg.text?.clear()
            binding.etPasswordRg.text?.clear()
            binding.etAgPasswordRg.text?.clear()

        }
    }
}



     /*fun registedUser() {
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
    }*/

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






