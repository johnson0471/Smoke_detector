package com.example.smoke_detector

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smoke_detector.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_temperature.*


class TemperatureFragment : Fragment() {
    private lateinit var database: DatabaseReference
    private val TAG = javaClass.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_temperature, container, false)
    }

    override fun onStart() {
        super.onStart()
        getData()
    }

    private fun getData() {
        //realtime的基本設定
        database = Firebase.database.reference
        //從子類別抓取溫度值
        database.child("test").child("溫度").child("5").get().addOnSuccessListener {
            if (it.exists()) {
                //如果存在，溫度就會顯示剛剛抓取的值
                val t = it.getValue().toString()
                tp_1.text = t
                tp_1.setTextColor(this.resources.getColor(R.color.green))
                Log.e(TAG, "Successful")
                val p = t.substring(0,t.indexOf("°C")).toInt()
                Log.e(TAG, "$p")
                val c = (p+2)/0.06
                progressBar.max = 1000
                val currentProgress = c.toInt()
                ObjectAnimator.ofInt(progressBar,"progress",currentProgress)
                    .setDuration(1000)
                    .start()
            } else
                {
                    tp_1.text = "未偵測"
                    tp_1.setTextColor(this.resources.getColor(R.color.red))
                }

        }
    }
}