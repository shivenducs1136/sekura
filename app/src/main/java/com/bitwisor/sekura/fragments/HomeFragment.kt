package com.bitwisor.sekura.fragments

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.bitwisor.sekura.R
import com.bitwisor.sekura.databinding.FragmentHomeBinding
import com.bitwisor.sekura.uitls.ShakeWorker
import java.util.*
import kotlin.math.sqrt


class HomeFragment : Fragment() {

    lateinit var binding:FragmentHomeBinding
    private lateinit var webView: WebView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uploadWorkRequest: WorkRequest =
            OneTimeWorkRequestBuilder<ShakeWorker>()
                .build()
        WorkManager
            .getInstance(requireContext())
            .enqueue(uploadWorkRequest)



        binding.womanHelpline.apply {
            loadUrl("http://ncw.nic.in/ncw-cells/Women-Safety-Cell")
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true



        }

        binding.mapBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_fragment_maps)
        }





    }


}