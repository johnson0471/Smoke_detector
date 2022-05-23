package com.example.smoke_detector

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smoke_detector.databinding.FragmentTemperatureBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class TemperatureFragment : Fragment() {


    private lateinit var database: DatabaseReference
    private val TAG = javaClass.simpleName
    private var _binding: FragmentTemperatureBinding? = null
    private val binding get() = _binding!!


    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTemperatureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dataChange()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        dataChange()
        Log.d(TAG, "onResume")
    }

    private fun dataChange() {
        database = Firebase.database.reference
        val dataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tpData = snapshot.value.toString()
                val tpDataInt = tpData.substring(0, tpData.indexOf("°C")).toInt()
                val dataJg = database.child("Judgment").child("temperature")
                dataJg.get().addOnSuccessListener {
                    val tpJg = it.value.toString().toInt()
                    if (tpDataInt >= tpJg) {
                        binding.tp1.text = tpData
                        binding.tp1.setTextColor(Color.RED)
                        Log.e(TAG, tpData)
                    } else if (tpDataInt < tpJg) {
                        binding.tp1.text = tpData
                        binding.tp1.setTextColor(Color.GREEN)
                        Log.e(TAG, tpData)
                    }
                    val c = (tpDataInt + 2) / 0.06
                    binding.progressBar.max = 1005
                    val currentProgress = c.toInt()
                    ObjectAnimator.ofInt(binding.progressBar, "progress", currentProgress)
                        .setDuration(1000)
                        .start()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
            }
        }
        database.child("test").child("溫度").child("5").addValueEventListener(dataListener)
    }
}






