package com.example.appcultural.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appcultural.databinding.ActivityLoginVisitBinding

class LoginVisitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginVisitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginVisitBinding.inflate(layoutInflater)
        setContentView(binding.main)
    }
}