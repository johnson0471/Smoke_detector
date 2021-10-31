package com.example.smoke_detector

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.smoke_detector.databinding.ActivityLoginBinding
import com.example.smoke_detector.databinding.ActivityPersonalBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class PersonalActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityPersonalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        setContentView(binding.root)
        binding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this,Control_Fragment_Activity::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        personalData()
    }

    private fun personalData(){
        val user = auth.currentUser
        user?.let {
            val name = user.displayName
            val email = user.email
            val emailVerify = user.isEmailVerified
            binding.tvName.text = name
            binding.tvEmail.text = email
            if (emailVerify) {
                binding.tvEmailVerify.text = "已驗證"
            }else binding.tvEmailVerify.text = "未驗證"
        }
    }
}