package com.example.appcultural.data

import java.util.Date
import com.example.appcultural.entities.ScheduleItem

class MockScheduleRepository {
    private val data: List<ScheduleItem> = mutableListOf(
        ScheduleItem("1", Date(), 10),
        ScheduleItem("2", Date(), 20),
        ScheduleItem("3", Date(), 10),
        ScheduleItem("4", Date(), 50),
        ScheduleItem("5", Date(), 30),
        ScheduleItem("6", Date(), 40)
    )

    fun list(): List<ScheduleItem> {
        return data
    }
}