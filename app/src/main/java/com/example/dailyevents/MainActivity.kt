package com.example.dailyevents

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.CalendarView
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    private lateinit var eventAdapter: EventAdapter
    private val eventMap = mutableMapOf<String, MutableList<Event>>() // Eventos por data
    private var selectedDate: String = LocalDate.now().toString() // Data selecionada no formato "yyyy-MM-dd"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        val rvEvents = findViewById<RecyclerView>(R.id.rvEvents)
        val btnAddEvent = findViewById<ImageView>(R.id.btnAddEvent)

        rvEvents.layoutManager = LinearLayoutManager(this)
        eventAdapter = EventAdapter(
            eventMap[selectedDate] ?: mutableListOf(),
            onEditClick = { position -> openEditEventActivity(position) },
            onDeleteClick = { position -> deleteEvent(position) }
        )
        rvEvents.adapter = eventAdapter

        // Configura o listener para o calendário
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val formattedDate = LocalDate.of(year, month + 1, dayOfMonth).toString()
            selectedDate = formattedDate
            updateEventsForSelectedDate()
        }

        // Adicionar um novo evento
        btnAddEvent.setOnClickListener {
            val intent = Intent(this, AddEventActivity::class.java)
            intent.putExtra("date", selectedDate)
            addEventLauncher.launch(intent)
        }

        window.statusBarColor = getColor(R.color.subColor)
    }

    // Atualiza os eventos da data selecionada
    private fun updateEventsForSelectedDate() {
        eventAdapter.updateEvents(eventMap[selectedDate] ?: mutableListOf())
    }

    private fun openEditEventActivity(position: Int) {
        val event = eventMap[selectedDate]?.get(position)
        val intent = Intent(this, EditEventActivity::class.java).apply {
            putExtra("title", event?.title)
            putExtra("description", event?.description)
            putExtra("time", event?.time)
            putExtra("position", position)
        }
        editEventLauncher.launch(intent)
    }

    private fun deleteEvent(position: Int) {
        eventMap[selectedDate]?.removeAt(position)
        updateEventsForSelectedDate()
    }

    private val editEventLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val updatedTitle = result.data?.getStringExtra("updatedTitle")
            val updatedDescription = result.data?.getStringExtra("updatedDescription")
            val updatedTime = result.data?.getStringExtra("updatedTime")
            val position = result.data?.getIntExtra("position", -1)
            if (updatedTitle != null && updatedDescription != null && updatedTime != null && position != null && position >= 0) {
                val updatedEvent = Event(updatedTitle, updatedDescription, updatedTime)
                eventMap[selectedDate]?.set(position, updatedEvent)
                updateEventsForSelectedDate()
            }
        }
    }

    private val addEventLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val title = result.data?.getStringExtra("title")
            val description = result.data?.getStringExtra("description")
            val time = result.data?.getStringExtra("time")
            val date = result.data?.getStringExtra("date")
            if (title != null && description != null && time != null && date != null) {
                val newEvent = Event(title, description, time)
                if (!eventMap.containsKey(date)) {
                    eventMap[date] = mutableListOf()
                }
                eventMap[date]?.add(newEvent)
                updateEventsForSelectedDate()
            }
        }
    }
}
