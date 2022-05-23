package com.example.smoke_detector

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.smoke_detector.databinding.FragmentCameraBinding
import android.webkit.WebViewClient
import com.google.firebase.database.*


class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private val TAG = javaClass.simpleName
    private val url = "http://192.168.216.82:5000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
        openCamera()
        syncData()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun openCamera() {
        binding.switchReset.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                playStream()
                Toast.makeText(
                    this.requireContext(),
                    "鏡頭已開啟", Toast.LENGTH_SHORT
                ).show()
            } else {
                binding.streamView.onPause()
                binding.streamView.removeView(binding.streamView)
                Toast.makeText(
                    this.requireContext(),
                    "鏡頭已關閉", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun syncData() {
        database = FirebaseDatabase.getInstance().reference
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child("on").value.toString().toInt() == 1) {
                    if (isAdded){
                        AlertDialog.Builder(this@CameraFragment.requireContext())
                            .setTitle("同步連線")
                            .setMessage("監控中心想要取得您的畫面權限")
                            .setCancelable(false)
                            .setPositiveButton("確定") { dialog, _ ->
                                database.child("串流網址").child("url").setValue(url)
                                dialog.cancel()
                            }
                            .setNegativeButton("取消") { dialog, _ ->
                                database.child("同步影像").child("on").setValue(0)
                            }
                            .show()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
            }
        }
        database.child("同步影像").addValueEventListener(postListener)

        binding.btnSync.setOnClickListener {
            database.child("同步影像").child("on").setValue(0)
            database.child("串流網址").child("url").removeValue()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun playStream() {
        val webSettings = binding.streamView.settings
        webSettings.javaScriptEnabled = true
        binding.streamView.onResume()
        binding.streamView.webViewClient = WebViewClient()
        binding.streamView.loadUrl(url)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.streamView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
        binding.streamView.clearHistory()
        binding.streamView.removeView(binding.streamView)
        binding.streamView.destroy()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    companion object {

    }
}