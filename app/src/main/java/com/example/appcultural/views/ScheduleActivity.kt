package com.example.appcultural.views

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Calendar
import java.text.SimpleDateFormat

import com.example.appcultural.data.FirebaseSchedulesRepository
import com.example.appcultural.databinding.ActivityScheduleBinding
import com.example.appcultural.entities.ScheduleItem

class ScheduleActivity : Fragment() {
    private lateinit var binding: ActivityScheduleBinding
    private lateinit var scheduleDate: Date

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityScheduleBinding.inflate(inflater, container, false)
        return binding.main
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.scheduleDateProvider.setOnClickListener {
            showDatePicker()
        }

        binding.scheduleAmountProvider.editText?.setOnEditorActionListener { _, actionId, _ ->  run {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                save()
            }
            false
        }}

        binding.saveButton.setOnClickListener {
            save()
        }
    }

    private fun save() {
        try {
            val repository = FirebaseSchedulesRepository()
            val count = binding.scheduleAmount.text.toString().toInt()
            lifecycleScope.launch {
                var schedule = repository.findByDate(scheduleDate)
                if (schedule == null) {
                    schedule = ScheduleItem("", scheduleDate, 0)
                    repository.add(schedule)
                }
                schedule.count += count
                repository.update(schedule)
                Toast.makeText(requireContext(), "Agendamento concluído", Toast.LENGTH_LONG).show()
            }
        } catch (err: Exception) {
            Toast.makeText(requireContext(), "Erro ao agendar: ${err.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
            showTimePicker(year, month, dayOfMonth)
        }, year, month, dayOfMonth)
        datePickerDialog.show()
    }

    private fun showTimePicker(year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
            run {
                calendar.set(year, month, dayOfMonth, hourOfDay, minute, 0)
                scheduleDate = calendar.time
                val format = SimpleDateFormat("dd/MM/yyyy 'às' HH:mm")
                binding.scheduleDateProvider.text = "Agendamento para: ${format.format(scheduleDate)}"
            }
        }, hour, minute, true)
        timePickerDialog.show()
    }
}
