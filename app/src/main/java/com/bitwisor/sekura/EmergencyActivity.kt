package com.bitwisor.sekura

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.navigation.fragment.findNavController
import com.bitwisor.sekura.databinding.ActivityEmergencyBinding

class EmergencyActivity : AppCompatActivity() {
    lateinit var binding:ActivityEmergencyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmergencyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.timertxt.text = ("Calling in: " + millisUntilFinished / 1000 +"s")
                // logic to set the EditText could go here
            }

            override fun onFinish() {
                startCall()
                binding.timertxt.text = ("Calling now!")
            }
        }.start()
        binding.cancelbtn.setOnClickListener {
            finish()
        }
        binding.callnowbtn.setOnClickListener {
            startCall()
        }

    }
    fun startCall(){
        val i = Intent(Intent.ACTION_CALL)
        i.data = Uri.parse("tel:9528881662")
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET or Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
        i.addFlags(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        startActivity(i)
    }
}