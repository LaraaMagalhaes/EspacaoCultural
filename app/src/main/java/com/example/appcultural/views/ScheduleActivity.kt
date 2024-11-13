// ScheduleActivity.kt
package com.example.appcultural.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.appcultural.databinding.ActivityScheduleBinding
import com.example.appcultural.entities.Schedule
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ScheduleActivity : Fragment() {
    private lateinit var binding: ActivityScheduleBinding
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa o Firebase Database
        database = FirebaseDatabase.getInstance().reference

        // Configura o listener para o botão "Salvar"
        binding.saveButton.setOnClickListener {
            saveSchedule()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun saveSchedule() {
        val date = binding.scheduleDate.text.toString()
        val hour = binding.scheduleHour.text.toString()
        val name = binding.scheduleName.text.toString()
        val amountText = binding.scheduleAmount.text.toString()

        if (date.isEmpty() || hour.isEmpty() || name.isEmpty() || amountText.isEmpty()) {
            Toast.makeText(context, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountText.toIntOrNull()
        if (amount == null) {
            Toast.makeText(context, "Quantidade inválida", Toast.LENGTH_SHORT).show()
            return
        }

        // Cria um ID único para o agendamento
        val scheduleId = database.child("schedules").push().key
        if (scheduleId == null) {
            Toast.makeText(context, "Erro ao gerar ID", Toast.LENGTH_SHORT).show()
            return
        }

        val schedule = Schedule(
            id = scheduleId,
            date = date,
            hour = hour,
            name = name,
            amount = amount
        )

        // Salva o agendamento no Firebase
        database.child("schedules").child(scheduleId).setValue(schedule)
            .addOnSuccessListener {
                Toast.makeText(context, "Agendamento salvo com sucesso", Toast.LENGTH_SHORT).show()
                // Limpa os campos após salvar
                binding.scheduleDate.text?.clear()
                binding.scheduleHour.text?.clear()
                binding.scheduleName.text?.clear()
                binding.scheduleAmount.text?.clear()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Erro ao salvar agendamento: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
