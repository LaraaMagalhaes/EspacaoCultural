package com.example.appcultural.data

import com.example.appcultural.entities.Art
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseArtsRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("arts")

    suspend fun add(item: Art): Art {
        val result = collection.add(item).await()
        item.id = result.id
        result.set(item).await()
        return item
    }

    suspend fun fetchAll(): List<Art> {
        val result = collection.get().await()
        return result.documents.mapNotNull { it.toObject(Art::class.java) }
    }

    suspend fun fetchByIds(ids: List<String>): List<Art> {
        val chunks = ids.chunked(10)
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
