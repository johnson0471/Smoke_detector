package com.example.smoke_detector

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MyViewModel :ViewModel() {

    var temperture = MutableLiveData<String>()
    var progressBar = MutableLiveData<Int>()
    private lateinit var databaseReference: DatabaseReference

}