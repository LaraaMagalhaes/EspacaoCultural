package com.example.appcultural.entities

import java.util.Date

data class ScheduleItem(
    var id: String = "schedules-" + System.currentTimeMillis(),
    val date: Date = Date(),
    val count: Int = 0
)
