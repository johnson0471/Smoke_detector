package com.example.smoke_detector

import android.app.Service
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyService : Service() {

    val TAG = MyService::class.java.simpleName
    val mBinder = ChatBinder()

    inner class ChatBinder: Binder(){
        fun getService() = this@MyService
    }
    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    fun sendMessage(message: String){
        Log.d(TAG,"sendMessage: $message")
    }

    fun deleteMessage(){
        Log.d(TAG,"deleteMessage")
    }


}