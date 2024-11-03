package com.example.appcultural.views

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appcultural.data.MockAuthProvider
import com.example.appcultural.databinding.ActivityLoginVisitBinding
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth


class LoginVisitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginVisitBinding
    private lateinit var auth: FirebaseAuth;
   // private lateinit var googleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginVisitBinding.inflate(layoutInflater)
        setContentView(binding.main)
        val authProvider = MockAuthProvider(this)

        binding.loginButton.setOnClickListener {
            authProvider.isAdmin = false

            logar(
                binding.editTextTextEmail.editText?.text.toString(),
                binding.loginEmployeeButton.editText?.text.toString())
        }

        binding.signupButton.setOnClickListener {
            val intent = Intent(this, SignUpVisitActivity::class.java)
            startActivity(intent)
        }

        binding.signupButtonGoogle.setOnClickListener{
            signIn()
        }



    }

    private fun signIn(){
    //    val intent = googleSignInClient.si
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
       // val currentUser = auth.currentUser
      //  updateUI(currentUser)
    }

    private fun logar(email: String, senha: String){
        auth = Firebase.auth

        auth?.let {
            auth.signInWithEmailAndPassword(email,senha)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "Autenticado com sucesso")
                        val user = auth.currentUser
                        val intent = Intent(this, BottomActivity::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCustomToken:failure", task.exception)
                        Toast.makeText(baseContext, "Autenticação falhou",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}