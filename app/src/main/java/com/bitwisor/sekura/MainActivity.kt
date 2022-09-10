package com.bitwisor.sekura

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bitwisor.sekura.databinding.ActivityLoginBinding
import com.bitwisor.sekura.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

       binding.logoutBtn.setOnClickListener {
           Firebase.auth.signOut()
           val intent = Intent(applicationContext, LoginActivity::class.java)
           startActivity(intent)
       }
    }

    private fun checkUser() {
       val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser == null)
        {
            startActivity(Intent(this@MainActivity,LoginActivity::class.java))
            finish()
        }
    }
}