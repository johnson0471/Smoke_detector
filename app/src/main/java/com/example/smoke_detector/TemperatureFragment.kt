package com.example.smoke_detector

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_temperature.*
import org.w3c.dom.Comment


class TemperatureFragment : Fragment() {
    private lateinit var database: DatabaseReference
    private val TAG = javaClass.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getData()
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
        //dataChange()
    }

    override fun onResume() {
        super.onResume()
        //setChildEvent()
    }


    private fun getData() {
        //realtime的基本設定
        val database = Firebase.database.reference
        database.child("Judgment").child("temperature").get().addOnSuccessListener {
            if (it.exists()) {
                val j = it.getValue().toString()
                Log.d(TAG, j)
                database.child("test").child("溫度").child("5").get().addOnSuccessListener {
                    if (it.exists()) {
                        //如果存在，溫度就會顯示剛剛抓取的值
                        val t = it.getValue().toString()
                        tp_1.text = t
                        val p = t.substring(0, t.indexOf("°C")).toInt()
                        if (p > j.toInt()) {
                            tp_1.setTextColor(this.resources.getColor(R.color.red))
                        } else {
                            tp_1.setTextColor(this.resources.getColor(R.color.green))
                        }
                        val c = (p + 2) / 0.06
                        progressBar.max = 1000
                        val currentProgress = c.toInt()
                        ObjectAnimator.ofInt(progressBar, "progress", currentProgress)
                            .setDuration(1000)
                            .start()
                    } else {
                        tp_1.text = "未偵測"
                        tp_1.setTextColor(this.resources.getColor(R.color.red))
                    }
                }
            }

        }

    }

    private fun dataChange() {
        val dbRef = Firebase.database
        val tp_data = dbRef.getReference("test/溫度/5")
        tp_data.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, error.toException().toString())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val tp_change = snapshot.getValue<String>()
                    tp_1.text = tp_change
                    setChildEvent()
                }
            }
        })
    }

    private fun setChildEvent() {
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.key!!)

                // A new comment has been added, add it to the displayed list
                val comment = dataSnapshot.getValue<Comment>()

                // ...
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.e(TAG, "onChildChanged: ${dataSnapshot.key}")

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                val newComment = dataSnapshot.getValue<Comment>()
                val commentKey = dataSnapshot.key

                // ...
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.e(TAG, "onChildRemoved:" + dataSnapshot.key!!)

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                val commentKey = dataSnapshot.key

                // ...
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.key!!)

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                val movedComment = dataSnapshot.getValue<Comment>()
                val commentKey = dataSnapshot.key

                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException())
                Toast.makeText(context, "Failed to load comments.",
                    Toast.LENGTH_SHORT).show()
            }
        }
        database = Firebase.database.reference.child("test").child("溫度").child("5")
        database.addChildEventListener(childEventListener)
    }
}


