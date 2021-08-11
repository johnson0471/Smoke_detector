package com.example.smoke_detector


import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.*

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class Activity_login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val input_account = findViewById<TextInputLayout>(R.id.input_account_login)
        val input_password = findViewById<TextInputLayout>(R.id.input_password_login)
        val et_account = findViewById<TextInputEditText>(R.id.et_account_login)
        val et_password = findViewById<TextInputEditText>(R.id.et_password_login)
        val btn_registerd = findViewById<Button>(R.id.btn_registered)
        val btn_login = findViewById<Button>(R.id.btn_login)
        val intent_temperature = Intent(this,Activity_temperature::class.java)
        val intent_registered = Intent(this,Activity_registered::class.java)

        btn_login.setOnClickListener {

            if (et_account.text.toString()
                    .isEmpty() || et_account.text.toString().length > 10  //若帳號輸入空或超過10個字，則提示字元顯示請輸入正確的帳號!!!，否則為空
            ) {
                input_account.error = "請輸入正確的帳號!!!"
            } else {
                input_account.error = null
            }

            if (et_password.text.toString()
                    .isEmpty() || et_password.text.toString().length > 10 //若密碼輸入空或超過20個字，則提示字元顯示請輸入正確的密碼!!!，否則為空
            ) {
                input_password.error = "請輸入正確的密碼!!!"
            } else {
                input_password.error = null
            }

            if (et_account.text.toString().isNotEmpty() && et_password.text.toString() //若帳號且密碼不為空，則跳出訊息登入成功，顯示對話框
                    .isNotEmpty()
            ) {
                Toast.makeText(this, "登入成功!!!", Toast.LENGTH_SHORT).show()
                startActivity(intent_temperature)

                /*val dialog = AlertDialog.Builder(this)
                val username = et_account.text.toString()

                dialog.setTitle("歡迎光臨")
                dialog.setMessage("恭喜 "+username+" 加入我們的會員~~~~")
                dialog.setCancelable(false)
                dialog.setPositiveButton("確定"){
                        dialog,which ->
                    startActivity(intent_temperature)
                }
                dialog.show()*/

            }else {
                Toast.makeText(this, "登入失敗!!!", Toast.LENGTH_SHORT).show() //否則跳出訊息登入失敗
            }
        }

        btn_registerd.setOnClickListener{
            startActivity(intent_registered)
        }
    }
}










