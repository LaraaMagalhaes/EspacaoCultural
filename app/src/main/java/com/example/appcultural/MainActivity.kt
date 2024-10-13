package com.example.appcultural

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appcultural.databinding.ActivityWelcomeBinding
import com.example.appcultural.views.LoginEmployeeActivity
import com.example.appcultural.views.LoginVisitActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.main)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.loginVisit.setOnClickListener {
            val intent = Intent(this, LoginVisitActivity::class.java)
            startActivity(intent)
        }

        binding.loginEmployee.setOnClickListener {
            val intent = Intent(this, LoginEmployeeActivity::class.java)
            startActivity(intent)
        }
    }
}