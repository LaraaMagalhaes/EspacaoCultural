package com.example.appcultural.views

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.appcultural.R
import com.example.appcultural.data.MockAuthProvider
import com.example.appcultural.databinding.ActivityLoginEmployeeBinding

class LoginEmployeeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginEmployeeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.main)
        val authProvider = MockAuthProvider(this)

        binding.loginEmployeeButton.setOnClickListener {
            authProvider.isAdmin = true
            val intent = Intent(this, BottomActivity::class.java)
            startActivity(intent)
        }

        binding.signupButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}