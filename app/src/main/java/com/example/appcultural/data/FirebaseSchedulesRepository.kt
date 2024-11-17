package com.example.appcultural.data

import com.example.appcultural.entities.ScheduleItem
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class FirebaseSchedulesRepository {
    private val db = Firebase.firestore
    private val collection = db.collection("schedules")

    suspend fun add(schedule: ScheduleItem): ScheduleItem {
        val result = collection.add(schedule).await()
        schedule.id = result.id
        result.set(schedule).await()
        return schedule
    }

    suspend fun list(): List<ScheduleItem> {
        val result = collection.get().await()
        return result.documents.mapNotNull { it.toObject(ScheduleItem::class.java) }
    }
}

