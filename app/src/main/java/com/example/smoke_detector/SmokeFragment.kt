package com.example.smoke_detector

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smoke_detector.databinding.FragmentSmokeBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class SmokeFragment : Fragment() {


    private var _binding: FragmentSmokeBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private val TAG = javaClass.simpleName


    override fun onResume() {
        super.onResume()
        dataSmoke()
    }

    override fun onStart() {
        super.onStart()
        dataSmoke()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSmokeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun dataSmoke() {
        database = Firebase.database.reference
        val dataListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val smData = dataSnapshot.value.toString().toInt()
                if (smData == 1) {
                    binding.tvStatus.text = "異常"
                    binding.tvStatus.setTextColor(Color.RED)
                    binding.ivSmoke.setImageResource(R.drawable.ic_warning)
                } else {
                    binding.tvStatus.text = "安全"
                    binding.tvStatus.setTextColor(Color.GREEN)
                    binding.ivSmoke.setImageResource(R.drawable.ic_safe)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "dataSmoke Error", databaseError.toException())
            }
        }
        database.child("test").child("煙霧").child("5").addValueEventListener(dataListener)
    }

}