package com.example.appcultural.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appcultural.databinding.ActivitySignupEmployeeBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupEmployeeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.main)
        binding.buttonCancel.setOnClickListener {
            finish()
        }
    }
}