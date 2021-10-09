package com.example.smoke_detector

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.util.Log
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintSet
import com.example.smoke_detector.R
import com.example.smoke_detector.databinding.ActivityStart2Binding
import com.example.smoke_detector.databinding.ActivityStartBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_start.*

class Activity_start : AppCompatActivity() {

    private lateinit var binding: ActivityStart2Binding
    private lateinit var auth: FirebaseAuth
    private val TAG = javaClass.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStart2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEnter.setOnClickListener{
            val intent = Intent(this,Activity_login::class.java)
            startActivity(intent)
            finish()
        }

        val constraintSet = ConstraintSet()
        constraintSet.clone(this,R.layout.activity_start)

        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 1000


        TransitionManager.beginDelayedTransition(binding.constraint, transition)
        constraintSet.applyTo(binding.constraint)
    }
    override fun onStart() {
        auth = FirebaseAuth.getInstance()
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        val intent = Intent(this,Control_Fragment_Activity::class.java)
        if (currentUser != null ){
            startActivity(intent)
            finish()
        }
    }
}