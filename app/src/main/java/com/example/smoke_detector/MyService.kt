package com.example.smoke_detector

import android.annotation.SuppressLint
import android.app.*
import android.app.PendingIntent.*

import android.content.Context
import android.content.Intent

import android.os.*
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import android.os.PowerManager

import android.app.KeyguardManager.KeyguardLock

import android.app.KeyguardManager
import android.app.Notification.VISIBILITY_PUBLIC
import android.os.PowerManager.WakeLock
import androidx.annotation.RequiresApi
import com.example.smoke_detector.MyWorker.Companion.CHANNEL_ID
import kotlin.system.exitProcess
import android.app.Service.VIBRATOR_SERVICE as VIBRATOR_SERVICE1


class MyService : Service() {

    private lateinit var database: DatabaseReference
    private val TAG = MyService::class.java.simpleName


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        cameraTesting()
        temperatureWarring()
        smokeWarring()
        residentialWarring()
        Log.e(TAG, "onStartCommand")
        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        Log.e(TAG, "onCreate")
    }


    private fun temperatureWarring() {
        database = Firebase.database.reference
        val dataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tp_data = snapshot.value.toString()
                val tp_data_int =
                    tp_data.substring(0, snapshot.value.toString().indexOf("°C")).toInt()
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

    private fun smokeWarring() {
        database = Firebase.database.reference
        val dataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val smData = snapshot.value.toString()
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

    private fun cameraTesting() {
        database = FirebaseDatabase.getInstance().reference
        val dataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val smData = snapshot.child("煙霧").child("5").value.toString().toInt()
                val tpData = snapshot.child("溫度").child("5").value.toString()
                database.child("Judgment").child("temperature").get().addOnSuccessListener {
                    val tpJudge = it.value.toString().toInt()
                    val tpInt = tpData.substring(0, tpData.indexOf("°C")).toInt()
                    while (smData == 1 ) {
                        if (tpInt>=tpJudge){
                            cameraDialog()
                            notifyFire()
                            Log.e(TAG,"cameraTesting")
                        }
                        break
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
            }
        }
        database.child("test").addValueEventListener(dataListener)
    }



    private fun cameraDialog() {
        vibratePhone()
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        val builder = AlertDialog.Builder(this, R.style.Theme_MaterialComponents_Dialog_Alert)
            .setIcon(R.drawable.ic_baseline_warning_24)
            .setTitle("火災警報")
            .setMessage("目前屋內溫度及煙霧警示異常\n請盡快開啟攝像監控並開啟鏡頭")
            .setCancelable(false)
            .setPositiveButton("確定") { dialog, _ ->
                vibrator.cancel()
                dialog.cancel()
            }
        val mAlertDialog = builder.create()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            mAlertDialog.window!!.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
        } else {
            mAlertDialog.window!!.setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG)
        }
        mAlertDialog.show()
    }


    private fun vibratePhone() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        val pattern = longArrayOf(200, 1000)
        vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0))
    }

    private fun residentialWarring() {
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

        manager.createNotificationChannel(channel)
        val builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("溫度警示")
            .setSmallIcon(R.drawable.zlz4)
            .setContentText("室內偵測溫度過高，請注意!!!")
            .setChannelId(channelId)
        manager.notify(0, builder.build())
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun notifySmoke() {
        val channelId = "smoke notification"
        val channelName = "煙霧警示通知"
        val manager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)

        manager.createNotificationChannel(channel)
        val builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("煙霧警示")
            .setSmallIcon(R.drawable.zlz4)
            .setContentText("室內偵測到煙霧，請注意!!!")
            .setChannelId(channelId)
        manager.notify(1, builder.build())
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun notifyResidential(num: Int) {
        val channelId = "resident notification"
        val channelName = "居家警示通知"
        val manager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        manager.createNotificationChannel(channel)
        val builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("居家警示")
            .setSmallIcon(R.drawable.zlz4)
            .setContentText("目前社區房屋編號${num}已發生火災，請注意!!!")
            .setChannelId(channelId)
        manager.notify(2, builder.build())
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun notifyFire() {
        val channelId = "fire notification"
        val channelName = "火災緊急通知"
        val manager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        manager.createNotificationChannel(channel)
        val builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("火災警示")
            .setSmallIcon(R.drawable.zlz4)
            .setContentText("疑似有火災現象，請注意!!!")
            .setChannelId(channelId)
        manager.notify(0, builder.build())
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy")
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}