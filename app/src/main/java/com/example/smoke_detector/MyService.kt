package com.example.smoke_detector

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.*
import android.app.PendingIntent.*
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.*
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyService : Service() {

    private lateinit var database: DatabaseReference
    private val TAG = MyService::class.java.simpleName
    private val binder = MainBinder()

    inner class MainBinder : Binder() {
        fun getService() = this@MyService
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        temperatureWarring()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    fun temperatureWarring() {
        database = Firebase.database.reference
        val dataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tp_data = snapshot.getValue<String>().toString()
                val tp_data_int = snapshot.value.toString()
                    .substring(0, snapshot.value.toString().indexOf("°C")).toInt()
                val database_jg = Firebase.database.reference.child("Judgment").child("temperature")
                database_jg.get().addOnSuccessListener {
                    val tp_jg = it.value.toString().toInt()
                    when {
                        tp_data_int >= tp_jg -> {
                            notifyTemperature()
                            Log.e(TAG, "Service $tp_data")
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
            }
        }
        database.child("test").child("溫度").child("5").addValueEventListener(dataListener)
    }

    fun smokeWarring() {
        database = Firebase.database.reference
        val dataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val smData = snapshot.getValue<String>().toString()
                when (smData.toInt()) {
                    1 -> {
                        notifySmoke()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
            }
        }
        database.child("test").child("煙霧").child("5").addValueEventListener(dataListener)
    }

    fun residentialWarring() {
        database = Firebase.database.reference
        val dataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val light1 = snapshot.child("R1").value.toString().toInt()
                val light2 = snapshot.child("R2").value.toString().toInt()
                val light3 = snapshot.child("R3").value.toString().toInt()
                val light4 = snapshot.child("R4").value.toString().toInt()
                val light5 = snapshot.child("R5").value.toString().toInt()
                val light6 = snapshot.child("R6").value.toString().toInt()
                val light7 = snapshot.child("R7").value.toString().toInt()
                val light8 = snapshot.child("R8").value.toString().toInt()
                val light9 = snapshot.child("R9").value.toString().toInt()

                when (light1) {
                    1 -> {
                        notifyResidential(1)
                    }
                }
                when (light2) {
                    1 -> {
                        notifyResidential(2)
                    }
                }
                when (light3) {
                    1 -> {
                        notifyResidential(3)
                    }
                }
                when (light4) {
                    1 -> {
                        notifyResidential(4)
                    }
                }
                when (light5) {
                    1 -> {
                        notifyResidential(5)
                    }
                }
                when (light6) {
                    1 -> {
                        notifyResidential(6)
                    }
                }
                when (light7) {
                    1 -> {
                        notifyResidential(7)
                    }
                }
                when (light8) {
                    1 -> {
                        notifyResidential(8)
                    }
                }
                when (light9) {
                    1 -> {
                        notifyResidential(9)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
            }
        }
        database.child("light").child("燈號").addValueEventListener(dataListener)
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    private fun notifyTemperature() {
        val channelId = "temperature notification"
        val channelName = "溫度過高通知"
        val manager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        val intent = Intent(this, Control_Fragment_Activity::class.java)
        val paddingIntent = getActivity(
            applicationContext,
            0,
            intent,
            FLAG_UPDATE_CURRENT
        )
        manager.createNotificationChannel(channel)
        val builder = NotificationCompat.Builder(this)
            .setContentTitle("溫度警示")
            .setSmallIcon(R.drawable.zlz4)
            .setContentText("室內溫度已經過高，請注意!!!")
            .setChannelId(channelId)
            .setContentIntent(paddingIntent)
        manager.notify(0, builder.build())
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun notifySmoke() {
        val channelId = "temperature notification"
        val channelName = "煙霧警示通知"
        val manager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        val intent = Intent(this, Control_Fragment_Activity::class.java)
        val paddingIntent = getActivity(
            applicationContext,
            1,
            intent,
            FLAG_UPDATE_CURRENT
        )
        manager.createNotificationChannel(channel)
        val builder = NotificationCompat.Builder(this)
            .setContentTitle("煙霧警示")
            .setSmallIcon(R.drawable.zlz4)
            .setContentText("室內偵測到煙霧，請注意!!!")
            .setChannelId(channelId)
            .setContentIntent(paddingIntent)
        manager.notify(1, builder.build())
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun notifyResidential(num: Int) {
        val channelId = "temperature notification"
        val channelName = "居家警示通知"
        val manager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        val intent = Intent(this, Control_Fragment_Activity::class.java)
        val paddingIntent = getActivity(
            applicationContext,
            2,
            intent,
            FLAG_UPDATE_CURRENT
        )
        manager.createNotificationChannel(channel)
        val builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("居家警示")
            .setSmallIcon(R.drawable.zlz4)
            .setContentText("目前社區房屋編號${num}已發生火災，請注意!!!")
            .setChannelId(channelId)
            .setContentIntent(paddingIntent)
        manager.notify(2, builder.build())
    }
}