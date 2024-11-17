package com.example.appcultural.entities

import java.util.Date

data class ScheduleItem(
    var id: String = "schedules-" + System.currentTimeMillis(),
    val date: Date = Date(),
    var count: Int = 0
)
