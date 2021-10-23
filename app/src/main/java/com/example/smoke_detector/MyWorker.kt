package com.example.smoke_detector

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class MyWorker(appContext: Context, workerParameters: WorkerParameters) :
    Worker(appContext, workerParameters) {

    companion object {
        private lateinit var sm_status: String
        private lateinit var database: DatabaseReference
        val TAG = MyWorker::class.java.simpleName
        const val CHANNEL_ID = "channel_id"
        const val NOTIFICATION_ID = 1
    }

    override fun doWork(): Result {
        Log.d(TAG, "doWork: Success function called")
        status()
        return Result.success()
    }

    private fun status() {
        database = Firebase.database.reference.child("test")
        val dataListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tp_data = dataSnapshot.child("溫度").child("5").value.toString()
                val sm_data = dataSnapshot.child("煙霧").child("5").value.toString().toInt()
                if (sm_data == 1) {
                    sm_status = "異常"
                    showNotification(tp_data, sm_status)
                } else {
                    sm_status = "安全"
                    showNotification(tp_data, sm_status)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "dataSmoke Error", databaseError.toException())
            }
        }
        database.addValueEventListener(dataListener)
    }


    //撰寫通知功能
    private fun showNotification(temperature: String, sm_status: String) {
        val intent = Intent(applicationContext, Control_Fragment_Activity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.zlz4)
            .setContentTitle("系統通知")
            .setContentText("現在溫度${temperature}，煙霧狀態${sm_status}")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val channelName = "Channel Name"
        val channelDescription = "Channel Description"
        val channelImportance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, channelName, channelImportance).apply {
            description = channelDescription
        }

        /*呼叫 getSystemService 取得 notificationManager 物件
        * 再轉型 NotificationManager
        * 然後呼叫 createNotificationChannel 加入頻道*/
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        with(NotificationManagerCompat.from(applicationContext)) {
            notify(NOTIFICATION_ID, notification.build())
        }
    }
}