package com.example.appcultural.data

import android.content.Context
import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseAuth

public class GoogleSignInClient(
    private val context : Context
) {
    private val tag = "GoogleSignInClient: "
    private val credentialManager = CredentialManager.create(context)
    private val firebaseAuth = FirebaseAuth.getInstance()



}