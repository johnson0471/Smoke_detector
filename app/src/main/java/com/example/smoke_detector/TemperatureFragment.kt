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
import androidx.fragment.app.activityViewModels
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val model: MyViewModel by activityViewModels()
        model.temperture.observe(this) {
            binding.tp1.text = it
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d(TAG, "onStop")
    }


    override fun onPause() {
        super.onPause()
        Log.d(TAG, " onPause")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach")
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
        val manager = this.context!!.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
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
        database = Firebase.database.reference.child("test").child("溫度").child("5")
        val dataListener = object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tp_data = dataSnapshot.getValue<String>().toString()
                val tp_data_int = tp_data.substring(0, tp_data.indexOf("°C")).toInt()
                val database_jg = Firebase.database.reference.child("Judgment").child("temperature")
                database_jg.get().addOnSuccessListener {
                    if (it.exists()) {
                        val tp_jg = it.value.toString().toInt()
                        if (tp_data_int >= tp_jg) {
                            binding.tp1.text = "$tp_data_int°C"
                            binding.tp1.setTextColor(Color.RED)
                            makeNotification()
                        } else {
                            binding.tp1.text = "$tp_data_int°C"
                            binding.tp1.setTextColor(Color.GREEN)
                        }
                    }
                    else Log.e(TAG, "temperature null")
                    val c = (tp_data_int + 2) / 0.06
                    binding.progressBar.max = 1000
                    val currentProgress = c.toInt()
                    ObjectAnimator.ofInt(progressBar, "progress", currentProgress)
                        .setDuration(1000)
                        .start()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        database.addValueEventListener(dataListener)
    }
}






