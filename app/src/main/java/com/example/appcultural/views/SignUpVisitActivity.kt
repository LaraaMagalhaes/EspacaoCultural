package com.example.appcultural.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appcultural.databinding.ActivitySignupVisitBinding

class SignUpVisitActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupVisitBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupVisitBinding.inflate(layoutInflater)
        setContentView(binding.main)

        binding.buttonCancel.setOnClickListener {
            finish()
        }
    }
}