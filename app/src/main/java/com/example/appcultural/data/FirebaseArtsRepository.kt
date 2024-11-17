package com.example.appcultural.data

import com.example.appcultural.entities.Art
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class FirebaseArtsRepository {
    private val db = Firebase.firestore
    private val collection = db.collection("arts")

    suspend fun list(): List<Art> {
        val result = collection.get().await()
        return result.documents.mapNotNull { it.toObject(Art::class.java) }
    }

    suspend fun findById(id: String): Art? {
        val result = collection.document(id).get().await()
        return result.toObject(Art::class.java)
    }

    suspend fun add(art: Art): Art {
        val result = collection.add(art).await()
        art.id = result.id
        result.set(art).await()
        return art
    }

    suspend fun update(art: Art): Art {
        collection.document(art.id).set(art).await()
        return art
    }
}