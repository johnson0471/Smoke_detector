package com.example.smoke_detector

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.smoke_detector.R
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase

class Activity_change : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change)

        val btn_determine = findViewById<Button>(R.id.btn_determine)
        btn_determine.setOnClickListener {
            val intent = Intent(this,Activity_login::class.java)
            startActivity(intent)
        }
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                }

                // Handle the deep link. For example, open the linked
                // content, or apply promotional credit to the user's
                // account.
                // ...

                // ...

                if (deepLink != null){
                    Log.e("Task","Successful")
                    Toast.makeText(this,"ssssss",Toast.LENGTH_SHORT).show()
                }else{
                    Log.e("Task","Error")
                    Toast.makeText(this,"ffffff",Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener(this) { e -> Log.w("Task", "getDynamicLink:onFailure", e) }
    }
}