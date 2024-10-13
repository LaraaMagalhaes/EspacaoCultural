package com.example.appcultural.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appcultural.databinding.ActivityLoginEmployeeBinding

class LoginEmployeeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginEmployeeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.main)
    }
}