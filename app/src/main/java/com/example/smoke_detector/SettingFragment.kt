package com.example.smoke_detector

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_setting.*
import java.util.*

class SettingFragment : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        databaseReference = Firebase.database.reference
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_setting)

        toolbar_st.setNavigationOnClickListener {
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
        btn_setTP.setOnClickListener {
            val tp_number = tp_et.text.toString()
            if (tp_number.isEmpty()) {
                tp_input.error = "請輸入溫度數值"
            } else {
                tp_input.error = null
                hideKeyboard(tp_et)
                databaseReference.child("Judgment").child("temperature").setValue(tp_number.toInt())
                Toast.makeText(this, "溫度設定成功", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun resetButton() {
        switch_reset.setOnClickListener {
            when {
                switch_reset.isChecked -> {
                    databaseReference.child("reset").child("ON").setValue(1)
                    Toast.makeText(this, "系統成功重置" + "狀態:1", Toast.LENGTH_SHORT).show()
                    Handler().postDelayed(
                        {
                            databaseReference.child("reset").child("ON").setValue(0)
                            switch_reset.isChecked = false
                            Toast.makeText(this, "系統回復重置" + "狀態:0", Toast.LENGTH_SHORT).show()
                        },
                        3000
                    )
                }
            }
        }
    }

    private fun Activity.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
