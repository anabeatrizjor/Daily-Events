package com.example.dailyevents

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    private lateinit var eventAdapter: EventAdapter
    private val eventMap = mutableMapOf<String, MutableList<Event>>() // Eventos por data
    private var selectedDate: String = LocalDate.now().toString() // Data selecionada no formato "yyyy-MM-dd"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val metasButton = findViewById<Button>(R.id.metasButton)
        val todosButton = findViewById<Button>(R.id.todosButton)

        metasButton.setOnClickListener {
           startActivity(Intent(this, Metas::class.java))
        }

        todosButton.setOnClickListener {
            Toast.makeText(this, "Você já está na aba 'Todos'", Toast.LENGTH_SHORT).show()
        }

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        val rvEvents = findViewById<RecyclerView>(R.id.rvEvents)
        val btnAddEvent = findViewById<ImageView>(R.id.btnAddEvent)

        eventAdapter = EventAdapter(mutableListOf(), onEditClick = { position ->
            openEditEventActivity(position)
        }, onDeleteClick = { position ->
            deleteEvent(position)
        })
        rvEvents.adapter = eventAdapter
        rvEvents.layoutManager = LinearLayoutManager(this)

        // Adicionar espaçamento de 10dp entre os itens
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.item_spacing)
        rvEvents.addItemDecoration(SpaceItemDecoration(spacingInPixels))

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

        loadEventsFromPreferences()

        window.statusBarColor = getColor(R.color.subColor)
    }

    private fun saveEventsToPreferences() {
        val sharedPreferences = getSharedPreferences("EventPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()

        val json = gson.toJson(eventMap)
        editor.putString("events", json)
        editor.apply() // Aplica as mudanças
    }

    private fun loadEventsFromPreferences() {
        val sharedPreferences = getSharedPreferences("EventPreferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("events", null)

        if (json != null) {
            val type = object : TypeToken<MutableMap<String, MutableList<Event>>>() {}.type
            val savedEvents: MutableMap<String, MutableList<Event>> = gson.fromJson(json, type)
            eventMap.putAll(savedEvents)
        }

        // Atualiza a lista de eventos com base na data selecionada
        updateEventsForSelectedDate()
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

        // Salva o estado atualizado nas preferências
        saveEventsToPreferences()
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

                // Salva o estado atualizado nas preferências
                saveEventsToPreferences()
            }
        }
    }

    private val addEventLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val title = result.data?.getStringExtra("title")
            val description = result.data?.getStringExtra("description")
            val time = result.data?.getStringExtra("time")
            val date = result.data?.getStringExtra("date")

            if (title != null && time != null && date != null) {
                Log.d("MainActivity", "Evento Recebido: $title para a data: $date")

                val newEvent = Event(title, description ?: "", time)
                eventMap[date]?.add(newEvent) ?: run {
                    eventMap[date] = mutableListOf(newEvent)
                }
                saveEventsToPreferences()  // Salva o evento nas preferências
                updateEventsForSelectedDate()
            } else {
                Log.e("MainActivity", "Data ou informações do evento estão nulas.")
            }
        }
    }
}
