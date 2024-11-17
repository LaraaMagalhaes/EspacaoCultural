package com.example.appcultural.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import com.example.appcultural.R
import com.example.appcultural.entities.ScheduleItem

class ScheduleListAdapter(private val data: List<ScheduleItem>):
    RecyclerView.Adapter<ScheduleListAdapter.ScheduleViewHolder>() {

    class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayText: TextView = itemView.findViewById(R.id.schedule_day)
        val countText: TextView = itemView.findViewById(R.id.visits_count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_schedule_item, parent, false)
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val schedule = data[position]
        val scheduleDate = schedule.date
        val format = SimpleDateFormat("dd/MM/yyyy 'às' HH:mm")
        holder.dayText.text = "Agendamento para: ${format.format(scheduleDate)}"
        holder.countText.text = "${schedule.count} visitandes"
    }

    override fun getItemCount(): Int = data.size
}
