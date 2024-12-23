package com.example.appcultural.views

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appcultural.databinding.ActivitySignupVisitBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.appcultural.data.FirebaseAuthProvider

class SignUpVisitActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupVisitBinding
    private lateinit var authProvider: FirebaseAuthProvider
    private val firestore = FirebaseFirestore.getInstance()

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
                registerUser(username, email, password)
            } else {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonCancel.setOnClickListener {
            finish()
        }
    }

    private fun registerUser(username: String, email: String, password: String) {
        authProvider.create(email, password) { success, errorMessage ->
            if (success) {
                val userId = FirebaseAuth.getInstance().currentUser?.uid

                if (userId != null) {
                    saveUsernameToFirestore(userId, username)
                }

                Toast.makeText(this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Falha no cadastro: ${errorMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUsernameToFirestore(userId: String, username: String) {
        firestore.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    val user = hashMapOf(
                        "username" to username
                    )
                    firestore.collection("users").document(userId)
                        .set(user)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Nome de usuário salvo com sucesso", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Erro ao salvar nome de usuário: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Nome de usuário já existe", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao verificar nome de usuário: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}
