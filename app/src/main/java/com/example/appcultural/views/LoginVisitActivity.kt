package com.example.appcultural.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appcultural.data.MockAuthProvider
import com.example.appcultural.databinding.ActivityLoginVisitBinding

class LoginVisitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginVisitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginVisitBinding.inflate(layoutInflater)
        setContentView(binding.main)
        val authProvider = MockAuthProvider(this)

        binding.loginButton.setOnClickListener {
            authProvider.isAdmin = false
            val intent = Intent(this, BottomActivity::class.java)
            startActivity(intent)
        }

        binding.signupButton.setOnClickListener {
            val intent = Intent(this, SignUpVisitActivity::class.java)
            startActivity(intent)
        }
    }
}