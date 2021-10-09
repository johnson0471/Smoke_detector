package com.example.smoke_detector

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.*
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.smoke_detector.databinding.FragmentTemperatureBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_temperature.*


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
        val view = binding.root
        return view
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

    private fun makeNotification() {
        val channelId = "temperature notification"
        val channelName = "溫度過高通知"
        val manager =
            this.requireContext().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        val intent = Intent(this.requireContext(), SmokeFragment::class.java)
        val paddingIntent = PendingIntent.getActivity(
            this.requireContext(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        manager.createNotificationChannel(channel)
        val builder = NotificationCompat.Builder(this.requireContext())
            .setContentTitle("智慧型住警器")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentText("溫度過高警示")
            .setChannelId(channelId)
            .setContentIntent(paddingIntent)
        manager.notify(1, builder.build())
    }


    private fun dataChange() {
        database = Firebase.database.reference
        val dataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tp_data = snapshot.getValue<String>().toString()
                val tp_data_int = snapshot.value.toString()
                    .substring(0, snapshot.value.toString().indexOf("°C")).toInt()
                val database_jg = Firebase.database.reference.child("Judgment").child("temperature")
                database_jg.get().addOnSuccessListener {
                    val tp_jg = it.value.toString().toInt()
                    if (tp_data_int >= tp_jg) {
                        binding.tp1.text = tp_data
                        binding.tp1.setTextColor(Color.RED)
                        Log.e(TAG, tp_data)
                        //makeNotification()
                    } else if (tp_data_int < tp_jg) {
                        binding.tp1.text = tp_data
                        binding.tp1.setTextColor(Color.GREEN)
                        Log.e(TAG, tp_data)
                        Log.e(TAG, "temperature safe")
                    }
                    val c = (tp_data_int + 2) / 0.06
                    binding.progressBar.max = 1005
                    val currentProgress = c.toInt()
                    ObjectAnimator.ofInt(progressBar, "progress", currentProgress)
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






