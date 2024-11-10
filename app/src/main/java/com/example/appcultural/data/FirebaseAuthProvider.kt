
package com.example.appcultural.data

import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthProvider {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // criar usuário com email e senha:)
    fun create(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }
}
