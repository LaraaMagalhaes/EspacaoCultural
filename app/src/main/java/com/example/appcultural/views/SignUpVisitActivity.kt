package com.example.appcultural.views

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appcultural.databinding.ActivitySignupVisitBinding
import com.google.firebase.auth.FirebaseAuth
import com.example.appcultural.data.FirebaseAuthProvider


class SignUpVisitActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupVisitBinding
    private lateinit var authProvider: FirebaseAuthProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupVisitBinding.inflate(layoutInflater)
        setContentView(binding.main)

        authProvider = FirebaseAuthProvider()

        binding.buttonSubmit.setOnClickListener {
            val username = binding.textInputLayoutUsername.editText?.text.toString()
            val email = binding.textInputLayoutEmail.editText?.text.toString()
            val password = binding.textInputLayoutPassword.editText?.text.toString()
            val confirmPassword = binding.textInputLayoutConfirmPassword.editText?.text.toString()

            if (password == confirmPassword) {
                registerUser(email, password)
            } else {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonCancel.setOnClickListener {
            finish()
        }
    }

    private fun registerUser(email: String, password: String) {
        authProvider.create(email, password) { success, errorMessage ->
        if (success) {
                    Toast.makeText(this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Falha no cadastro: ${errorMessage}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}