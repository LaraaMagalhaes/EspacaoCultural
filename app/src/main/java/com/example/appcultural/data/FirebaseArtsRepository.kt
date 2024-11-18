package com.example.appcultural.data

import com.example.appcultural.entities.Art
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseArtsRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("arts")

    // Adiciona uma nova arte
    suspend fun add(art: Art): Art {
        val result = collection.add(art).await()
        art.id = result.id // Atualiza o ID com o gerado pelo Firestore
        return art
    }

    suspend fun list(): List<Art> {
        val result = collection.get().await()
        return result.documents.mapNotNull { it.toObject(Art::class.java) }
    }

    suspend fun getArtsByIds(artIds: List<String>): List<Art> {
        val chunks = artIds.chunked(10) // Firestore limita whereIn a 10 elementos
        val arts = mutableListOf<Art>()
        for (chunk in chunks) {
            val result = collection.whereIn(FieldPath.documentId(), chunk).get().await()
            arts.addAll(result.documents.mapNotNull {
                val art = it.toObject(Art::class.java)
                art?.id = it.id
                art
            })
        }
        return arts
    }
}
