package com.example.smoke_detector

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.example.smoke_detector.R.layout.*
import com.example.smoke_detector.databinding.ActivityChangeBinding.inflate
import com.example.smoke_detector.databinding.ActivityForgotBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.*
import com.google.firebase.ktx.Firebase

class Activity_forgot : AppCompatActivity() {

    private lateinit var binding: ActivityForgotBinding
    private val TAG = javaClass.simpleName
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.btnDetermine.setOnClickListener {
            sendVerifyEmail()
        }

        binding.toolbarForgot.setNavigationOnClickListener {
            startActivity(Intent(this, Activity_login::class.java))
            finish()
        }


    }


    private fun sendVerifyEmail() {
        auth = FirebaseAuth.getInstance()

        val input_email = findViewById<TextInputLayout>(R.id.input_email_fr)
        val et_email = findViewById<TextInputEditText>(R.id.et_email_fr)
        val emailAddress = et_email.text.toString()
        val user = Firebase.auth

        if (emailAddress.isEmpty()) {
            input_email.error = "請重新輸入電子郵件!!!"
        } else if (emailAddress.isNotEmpty()) {
            if (Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                input_email.error = null
                user.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.e(TAG, "Successful")
                            Toast.makeText(this, "驗證郵件已成功寄出!!!", Toast.LENGTH_SHORT).show()
                            bottomSheet_dialog()

                        } else {
                            Toast.makeText(this, "此用戶尚未註冊!!!", Toast.LENGTH_SHORT).show()
                            Log.e(TAG, "Error " + task.exception)
                        }
                    }
            } else {
                Toast.makeText(this, "驗證碼寄送失敗", Toast.LENGTH_SHORT).show()
                input_email.error = "輸入格式有誤，請再次確認您的電子郵件"
            }
        }
    }

     private fun bottomSheet_dialog() {

        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheet = layoutInflater.inflate(bottom_sheet,null,false)
        val btn_cancel = bottomSheet.findViewById<MaterialButton>(R.id.btn_cancel_bottomSheet)
        val btn_gmail = bottomSheet.findViewById<LinearLayout>(R.id.btn_gmail)
        val btn_yahoo = bottomSheet.findViewById<LinearLayout>(R.id.btn_yahoo)


         bottomSheetDialog.setContentView(bottomSheet)
         bottomSheetDialog.show()

         btn_cancel.setOnClickListener {
             bottomSheetDialog.dismiss()
         }

         btn_gmail.setOnClickListener{
             val Intent = packageManager.getLaunchIntentForPackage("com.google.android.gm")
             startActivity(Intent)
             finish()
         }
         btn_yahoo.setOnClickListener{
             val Intent = packageManager.getLaunchIntentForPackage("com.yahoo.mobile.client.android.mail")
             startActivity(Intent)
             finish()
         }
    }
}



