package com.example.smoke_detector

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smoke_detector.databinding.FragmentSettingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SettingFragment : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentSettingBinding
    private lateinit var databaseReference: DatabaseReference
    private var text = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        databaseReference = Firebase.database.reference
        super.onCreate(savedInstanceState)
        binding = FragmentSettingBinding.inflate(layoutInflater)
        auth = Firebase.auth
        setContentView(binding.root)

        binding.toolbarSt.setNavigationOnClickListener {
            startActivity(Intent(this, Control_Fragment_Activity::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        setDataTP()
        resetButton()
    }

    private fun setDataTP() {
        binding.btnSetTP.setOnClickListener {
            val tpNumber = binding.tpEt.text.toString()
            if (tpNumber.isEmpty()) {
                binding.tpInput.error = "請輸入溫度數值"
            } else {
                binding.tpInput.error = null
                hideKeyboard(binding.tpEt)
                databaseReference.child("Judgment").child("temperature").setValue(tpNumber.toInt())
                Toast.makeText(this, "溫度設定成功", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun resetButton() {
        binding.switchReset.setOnClickListener {
            if (it.isClickable) {
                AlertDialog.Builder(this)
                    .setTitle("警報解除")
                    .setMessage("確定要解除警報嗎")
                    .setCancelable(false)
                    .setPositiveButton("確定") { dialog, which ->
                        alertdialog()
                    }
                    .setNegativeButton("取消") { dialog, which ->
                        binding.switchReset.isChecked = false
                    }
                    .show()
            }
        }
    }

    private fun alertdialog() {
        val input = EditText(this)
        input.hint = "請輸入密碼"
        input.inputType = InputType.TYPE_CLASS_TEXT
        AlertDialog.Builder(this)
            .setTitle("警報解除")
            .setMessage("若要解除，請輸入您的密碼")
            .setView(input)
            .setCancelable(false)
            .setPositiveButton("確定") { dialog, which ->
                text = input.text.toString()
                databaseReference.child("已登入").child("password").get().addOnSuccessListener {
                    val password = it.value.toString()
                    if (password == text) {
                        databaseReference.child("reset").child("ON").setValue(1)
                        databaseReference.child("同步影像").child("ON").setValue(0)
                        Toast.makeText(this, "系統成功重置" + "狀態:1", Toast.LENGTH_SHORT).show()
                        Handler().postDelayed(
                            {
                                databaseReference.child("reset").child("ON").setValue(0)
                                binding.switchReset.isChecked = false
                                Toast.makeText(this, "系統回復重置" + "狀態:0", Toast.LENGTH_SHORT).show()
                            },
                            3000
                        )
                    } else {
                        Toast.makeText(this, "密碼錯誤，系統重置失敗", Toast.LENGTH_SHORT).show()
                        binding.switchReset.isChecked = false
                    }
                }

            }
            .setNegativeButton("取消") { dialog, which -> binding.switchReset.isChecked = false }
            .show()
    }

    private fun Activity.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
