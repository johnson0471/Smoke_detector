package com.example.smoke_detector

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.widget.MediaController
import android.widget.Toast
import com.example.smoke_detector.databinding.FragmentCameraBinding
import android.webkit.WebViewClient


class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private val TAG = javaClass.simpleName
    private val src = "http://192.168.0.104:5000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onStart() {
        super.onStart()
        openCamera()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
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
                Toast.makeText(
                    this.requireContext(),
                    "鏡頭已關閉", Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun playStream() {
        val webSettings = binding.streamView.settings
        webSettings.javaScriptEnabled = true
        binding.streamView.onResume()
        binding.streamView.webViewClient = WebViewClient()
        binding.streamView.loadUrl(src)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.streamView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
        binding.streamView.clearHistory()
        binding.streamView.removeView(binding.streamView)
        binding.streamView.destroy()
    }



    companion object {

    }
}