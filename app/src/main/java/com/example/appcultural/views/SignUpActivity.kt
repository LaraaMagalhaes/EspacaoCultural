package com.example.appcultural.views

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appcultural.databinding.ActivitySignupEmployeeBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupEmployeeBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configura o view binding
        binding = ActivitySignupEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.main)

        // Inicializa o FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Configura o botão de cancelamento
        binding.buttonCancel.setOnClickListener {
            finish()
        }

        // Configura o botão de envio do cadastro
        binding.buttonSubmit.setOnClickListener {
            val name = binding.textInputLayoutName.editText?.text.toString()
            val registration = binding.textInputLayoutRegistration.editText?.text.toString()
            val password = binding.textInputLayoutPassword.editText?.text.toString()
            val confirmPassword = binding.textInputLayoutConfirmPassword.editText?.text.toString()

            if (password == confirmPassword) {
                registerEmployee(name, registration, password)
            } else {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerEmployee(name: String, registration: String, password: String) {
        // Vamos usar o registration como um "identificador" para criação no Firebase, por exemplo
        auth.createUserWithEmailAndPassword("$registration@empresa.com", password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Cadastro de funcionário realizado com sucesso", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Falha no cadastro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
