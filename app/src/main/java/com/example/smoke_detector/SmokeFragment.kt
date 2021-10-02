package com.example.smoke_detector

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.NotificationCompat
import com.example.smoke_detector.R
import com.example.smoke_detector.databinding.FragmentSmokeBinding
import com.example.smoke_detector.databinding.FragmentTemperatureBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_smoke.*

class SmokeFragment : Fragment() {

    companion object {
        private var _binding: FragmentSmokeBinding? = null
        private val binding get() = _binding!!
        private lateinit var database: DatabaseReference
        private val TAG = javaClass.simpleName

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataSmoke()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSmokeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    private fun dataSmoke() {
        database = Firebase.database.reference.child("test").child("煙霧").child("5")
        val dataListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val sm_data = dataSnapshot.getValue<String>()
                val s = sm_data!!.toInt()
                if (s == 1){
                    binding.tvStatus.text = "異常"
                    binding.tvStatus.setTextColor(Color.RED)
                    binding.ivSmoke.setImageResource(R.drawable.ic_warning)
                    makeNotification()
                }else{
                    binding.tvStatus.text = "安全"
                    binding.tvStatus.setTextColor(Color.GREEN)
                    binding.ivSmoke.setImageResource(R.drawable.ic_safe)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "dataSmoke Error", databaseError.toException())
            }
        }
        database.addValueEventListener(dataListener)

    }

    private fun makeNotification() {
        val channelId = "smoke notification"
        val channelName = "煙霧偵測通知"
        val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_HIGH)
        val intent = Intent(this.requireContext(),Control_Fragment_Activity::class.java)
        val paddingIntent = PendingIntent.getActivity(this.requireContext(),0,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        manager.createNotificationChannel(channel)
        val builder = NotificationCompat.Builder(this.requireContext())
            .setSmallIcon(R.drawable.zlz4)
            .setContentIntent(paddingIntent)
            .setContentTitle("智慧型住警器")
            .setContentText("煙霧偵測警示")
            .setChannelId(channelId)
        manager.notify(2,builder.build())
    }



}