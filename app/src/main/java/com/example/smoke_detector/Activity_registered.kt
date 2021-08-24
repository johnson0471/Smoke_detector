package com.example.smoke_detector


import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.smoke_detector.databinding.ActivityRegisteredBinding
import com.example.smoke_detector.databinding.ActivityRegisteredBinding.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class Activity_registered : AppCompatActivity() {

    private lateinit var binding: ActivityRegisteredBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        binding = inflate(layoutInflater)
        setContentView(binding.root)
        val btn_register = binding.btnRegistered2
        btn_register.setOnClickListener {
            register_User()
        }
    }

    private fun register_User() {
        user_error()
    }


    private fun sendUsers() {

        val username = binding.etNameRg.text.toString()
        val email = binding.etEmailRg.text.toString()
        val password = binding.etPasswordRg.text.toString()

        database = Firebase.database.reference
        val User = user(username, email , password)
        database.child("Users").child(username).setValue(User).addOnSuccessListener {

            binding.etNameRg.text?.clear()
            binding.etEmailRg.text?.clear()
            binding.etPasswordRg.text?.clear()
            binding.etAgPasswordRg.text?.clear()

        }
    }


    private fun updateUserAuth() {

        val email_auth = binding.etEmailRg.text.toString()
        val password_auth = binding.etPasswordRg.text.toString()
        val intent_login = Intent(this, Activity_login::class.java)

        auth.createUserWithEmailAndPassword(email_auth, password_auth)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener{
                            if (task.isSuccessful) {
                                Log.d("Task message", "Email sent.")
                                sendUsers()
                                Toast.makeText(this, "恭喜您成為我們的會員~~", Toast.LENGTH_SHORT).show()
                                startActivity(intent_login)
                                finish()
                            }
                        }
                    Log.e("Task message", "Registerd Successful")
                } else {
                    Log.e("Task message", "Failed " + task.exception)
                    Toast.makeText(this, "帳戶電子郵件已有使用者註冊", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun user_error() {

        val input_name = binding.inputNameRg
        val input_email = binding.inputEmailRg
        val input_password = binding.inputPasswordRg
        val input_agpassword = binding.inputAgPasswordRg

        val username = binding.etNameRg.text.toString()
        val email = binding.etEmailRg.text.toString()
        val password = binding.etPasswordRg.text.toString()
        val ag_password = binding.etAgPasswordRg.text.toString()

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || ag_password.isEmpty()) {

            when {
                username.isEmpty() -> {
                    input_name.error = "名字欄位不能為空"
                }
                username.length >= 10 -> {
                    input_name.error = "名字欄位不能超過10個字"
                }
                else -> input_name.error = null
            }

            when {
                email.isEmpty() -> {
                    input_email.error = "電子郵件欄位不能為空"
                }
                email.isNotEmpty() -> {
                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        input_email.error = null
                    } else input_email.error = "電子郵件格式有誤"
                }
            }

            when {
                password.isEmpty() -> {
                    input_password.error = "密碼欄位不能為空"
                }
                password.length < 6 -> {
                    input_password.error = "密碼欄位不能小於6個字"
                }
                else -> input_password.error = null
            }

            when {
                ag_password.isEmpty() -> {
                    input_agpassword.error = "請再次輸入正確的密碼"
                }
                ag_password != password -> {
                    input_agpassword.error = "確認密碼與密碼不相符，請再次輸入正確的密碼"
                }
                ag_password.length < 6 -> {
                    input_agpassword.error = "確認密碼與密碼不相符，且不能小於6個字"
                }
                else -> input_agpassword.error = null
            }

        } else if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && ag_password.isNotEmpty()) {

            if (username.length >= 10) {
                input_name.error = "名字欄位不能超過10個字"
            } else input_name.error = null

            if (password.length < 6) {
                input_password.error = "密碼欄位不能小於6個字"
            } else input_password.error = null

            when {
                ag_password != password -> {
                    input_agpassword.error = "請再次輸入正確的密碼"
                }
                ag_password.length < 6 -> {
                    input_agpassword.error = "確認密碼與密碼不相符，且不能小於6個字"
                }
                else -> input_agpassword.error = null
            }

            if (username.length < 10 && password.length >= 6) {
                input_name.error = null
                input_password.error = null
                if (ag_password == password) {
                    input_agpassword.error = null
                    updateUserAuth()
                }
            }
        }
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






