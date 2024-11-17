package com.example.appcultural.data

import kotlinx.coroutines.tasks.await
import java.util.Date
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore

import com.example.appcultural.entities.ScheduleItem

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

    suspend fun findByDate(date: Date): ScheduleItem? {
        val result = collection.whereEqualTo("date", Timestamp(date)).get().await().documents
        if (result.isEmpty()) return null
        return result[0].toObject(ScheduleItem::class.java)
    }

    suspend fun update(schedule: ScheduleItem): ScheduleItem {
        collection.document(schedule.id).set(schedule).await()
        return schedule
    }
}

