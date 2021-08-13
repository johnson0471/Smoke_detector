package com.example.smoke_detector


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.example.smoke_detector.databinding.ActivityRegisteredBinding
import com.example.smoke_detector.databinding.ActivityRegisteredBinding.*

import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.database.DatabaseReference



class Activity_registered : AppCompatActivity() {

    private lateinit var binding: ActivityRegisteredBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        binding = inflate(layoutInflater)
        val btn_register = binding.btnRegistered2
        setContentView(binding.root)

        btn_register.setOnClickListener {
            register_User()
        }
    }

    fun register_User() {
        usererror()
    }


    /*fun sendUsers() {
val username = binding.etNameRg.text.toString()
val email = binding.etEmailRg.text.toString()

database = Firebase.database.reference
val User = user(username, email)
database.child("Users").child(username).setValue(User).addOnSuccessListener{


    Toast.makeText(this,"使用者註冊成功",Toast.LENGTH_SHORT).show()
    binding.etNameRg.text?.clear()
    binding.etEmailRg.text?.clear()
    binding.etPasswordRg.text?.clear()
    binding.etAgPasswordRg.text?.clear()

}
}*/


    /*fun updateUserAuth() {

 val email_auth = binding.etEmailRg.text.toString()
 val password_auth = binding.etPasswordRg.text.toString()
 val intent_login = Intent(this,Activity_login::class.java)
 auth.createUserWithEmailAndPassword(email_auth,password_auth)
    .addOnCompleteListener(this){
        task ->
        if (task.isSuccessful){
            Log.e("Task message","Successful")
            sendUsers()
            startActivity(intent_login)
        }else{
            Log.e("Task message","Failed "+task.exception)
        Toast.makeText(this, "這個帳戶已有使用者註冊,或密碼小於6個字", Toast.LENGTH_SHORT).show()
        }
    }
}*/

    fun usererror() {
        val input_name = binding.inputNameRg
        val input_email = binding.inputEmailRg
        val input_password = binding.inputPasswordRg
        val input_agpassword = binding.inputAgPasswordRg
        val intent = Intent(this, Activity_login::class.java)

        val username = binding.etNameRg.text.toString()
        val email = binding.etEmailRg.text.toString()
        val password = binding.etPasswordRg.text.toString()
        val ag_password = binding.etAgPasswordRg.text.toString()

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || ag_password.isEmpty()) {

            if (username.isEmpty()) {
                input_name.error = "名字欄位不能為空"
            } else if (username.length >= 10){
                input_name.error = "名字欄位不能超過10個字"
            }else input_name.error = null

            if (email.isEmpty()) {
                input_email.error = "電子郵件欄位不能為空"
            } else input_email.error = null

            if (password.isEmpty()) {
                input_password.error = "密碼欄位不能為空"
            } else if (password.length >= 10){
                input_password.error = "密碼欄位不能超過10個字"
            }
            else input_password.error = null

            if (ag_password.isEmpty()) {
                input_agpassword.error = "請再次輸入正確的密碼"
            } else if (ag_password != password) {
                input_agpassword.error = "確認密碼與密碼不相符"
            }
            else input_agpassword.error = null

        } else if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && ag_password.isNotEmpty()) {

            if (username.isEmpty()) {
                input_name.error = "名字欄位不能為空"
            } else if (username.length >= 10){
                input_name.error = "名字欄位不能超過10個字"
            }else input_name.error = null

            if (email.isEmpty()) {
                input_email.error = "電子郵件欄位不能為空"
            } else input_email.error = null

            if (password.isEmpty()) {
                input_password.error = "密碼欄位不能為空"
            } else if (password.length >= 10){
                input_password.error = "密碼欄位不能超過10個字"
            }
            else input_password.error = null

            if (ag_password.isEmpty()) {
                input_agpassword.error = "請再次輸入正確的密碼"
            } else if (ag_password != password) {
                input_agpassword.error = "確認密碼與密碼不相符"
            }
            else input_agpassword.error = null

            if (username.length < 10 && password.length < 10) {
                input_name.error = null
                input_password.error = null
                if (ag_password == password) {
                    input_agpassword.error = null
                    startActivity(intent)
                    Toast.makeText(this,"恭喜會員註冊成功!!!",Toast.LENGTH_SHORT).show()
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






