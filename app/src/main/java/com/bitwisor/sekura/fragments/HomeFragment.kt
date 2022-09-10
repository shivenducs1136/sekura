package com.bitwisor.sekura.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.bitwisor.sekura.EmergencyActivity
import com.bitwisor.sekura.R
import com.bitwisor.sekura.databinding.FragmentHomeBinding
import com.bitwisor.sekura.uitls.ShakeWorker
import java.util.*


class HomeFragment : Fragment() {

    lateinit var binding:FragmentHomeBinding

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
        binding.callbtn.setOnClickListener {
           val i = Intent(requireActivity(),EmergencyActivity::class.java)
            requireActivity().startActivity(i)
        }

    }

}