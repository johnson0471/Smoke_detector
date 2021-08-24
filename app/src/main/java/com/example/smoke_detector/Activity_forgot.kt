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
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Activity_forgot : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)
        val intent = Intent(this, Activity_verification::class.java)
        val btn_determine = findViewById<Button>(R.id.btn_determine)

        btn_determine.setOnClickListener {
            sendVerifyEmail()
        }


    }



    private fun sendVerifyEmail(){
        val user = Firebase.auth.currentUser
        val input_email = findViewById<TextInputLayout>(R.id.input_email_fr)
        val et_email = findViewById<TextInputEditText>(R.id.et_email_fr)
        val emailAddress = et_email.text.toString()



        if (emailAddress.isEmpty()){
            input_email.error = "請重新輸入電子郵件!!!"
        }else if (emailAddress.isNotEmpty()){
            if (Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
                if (emailAddress == user?.email){
                    input_email.error = null
                    user.sendEmailVerification()
                        .addOnCompleteListener{
                                task ->
                            if (task.isSuccessful){
                                Log.e("Task", "Successful")
                                Toast.makeText(this, "驗證郵件已成功寄出!!!", Toast.LENGTH_SHORT).show()
                            }else{
                                Log.e("Task", "Error " + task.exception)
                            }
                        }
                }else Toast.makeText(this, "此用戶尚未註冊!!!", Toast.LENGTH_SHORT).show()
            }else  input_email.error = "輸入格式有誤，請再次確認您的電子郵件"
        }
    }

    /*private fun email_link(){
        val auth = Firebase.auth
        val user = auth.currentUser!!
        val actionCodeSettings = ActionCodeSettings.newBuilder()
            .setHandleCodeInApp(true)
            .setAndroidPackageName("com.example.smoke_detector",false,null)
    }*/
}