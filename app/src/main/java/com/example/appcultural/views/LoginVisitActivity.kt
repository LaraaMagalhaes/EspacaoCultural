package com.example.appcultural.views

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appcultural.data.MockAuthProvider
import com.example.appcultural.databinding.ActivityLoginVisitBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginVisitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginVisitBinding
    private lateinit var auth: FirebaseAuth // FirebaseAuth instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configura o binding
        binding = ActivityLoginVisitBinding.inflate(layoutInflater)
        setContentView(binding.main)

        // Inicializa o FirebaseAuth
        auth = FirebaseAuth.getInstance()

        val authProvider = MockAuthProvider(this)

        // Configura o botão de login
        binding.loginButton.setOnClickListener {
            authProvider.isAdmin = false
            logar(
                binding.editTextTextEmail.editText?.text.toString(),
                binding.loginEmployeeButton.editText?.text.toString()
            )
        }

        // Configura o botão de cadastro
        binding.signupButton.setOnClickListener {
            val intent = Intent(this, SignUpVisitActivity::class.java)
            startActivity(intent)
        }

        // Configura o botão de login com Google (adicione a lógica do GoogleSignIn aqui)
        binding.signupButtonGoogle.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        // Adicione a lógica de login com Google aqui se necessário
    }

    public override fun onStart() {
        super.onStart()
        // Verifica se o usuário está logado e atualiza a UI se necessário
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun logar(email: String, senha: String) {
        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login bem-sucedido
                    Log.d(TAG, "Autenticado com sucesso")
                    val intent = Intent(this, BottomActivity::class.java)
                    startActivity(intent)
                } else {
                    // Falha no login
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Autenticação falhou", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        // Atualize a interface do usuário de acordo com o estado do usuário logado
    }
}
