package com.example.appcultural.data

import com.example.appcultural.entities.ScheduleItem

class MockScheduleRepository {
    private val data: List<ScheduleItem> = mutableListOf(
        ScheduleItem(1, "01/09/2024", 10),
        ScheduleItem(2, "02/09/2024", 20),
        ScheduleItem(3, "03/09/2024", 10),
        ScheduleItem(4, "04/09/2024", 50),
        ScheduleItem(5, "05/09/2024", 30),
        ScheduleItem(6, "06/09/2024", 40)
    )

    fun list(): List<ScheduleItem> {
        return data
    }
}