package com.bitwisor.sekura.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.bitwisor.sekura.EmergencyActivity
import com.bitwisor.sekura.R
import com.bitwisor.sekura.databinding.FragmentHomeBinding
import com.bitwisor.sekura.uitls.ShakeWorker
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class HomeFragment : Fragment() {

    lateinit var binding:FragmentHomeBinding
    lateinit var auth:FirebaseAuth
    var num ="112"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        if(auth.currentUser!=null){
            Glide.with(requireContext())
                .load(auth.currentUser!!.photoUrl.toString())
                .into(binding.profileImg);
        }

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

        binding.locateBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_fragment_maps)
        }
        val languages = resources.getStringArray(R.array.helpline_nos)
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_layout, languages)
        val autocompleteTV = view.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        autocompleteTV.setAdapter(arrayAdapter)
        autocompleteTV.addTextChangedListener {
            when(it.toString()) {
                "POLICE" -> num = "100"
                "FIRE" -> num = "101"
                "AMBULANCE" -> num = "102"
                "Women Helpline" -> num = "1091"
                "Women Helpline - ( Domestic Abuse )" -> num = "181"
                "Anti Poison ( New Delhi )" -> num = "1066"
                "Senior Citizen Helpline" -> num = "14567"
                "Tourist Helpline" -> num = "1363"
                "CYBER CRIME HELPLINE" -> num = "155620"
                else -> num = "112"
            }
        }
        binding.callgreen.setOnClickListener {

            callNum(num)
        }
        binding.bluecard.setOnClickListener {
            var packageName = "com.DefaultCompany.sekura"
            var intent = requireContext().packageManager.getLaunchIntentForPackage(packageName)
            if (intent == null) {
                // Bring user to the market or let them choose an app?
                intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("market://details?id=$packageName")
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            requireContext().startActivity(intent)
        }

    }
    fun callNum(num:String){
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:${num}")
        startActivity(callIntent)
    }
}