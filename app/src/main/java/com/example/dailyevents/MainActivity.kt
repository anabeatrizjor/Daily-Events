package com.example.dailyevents

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.CalendarView
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    private lateinit var eventAdapter: EventAdapter
    private val eventMap = mutableMapOf<String, MutableList<String>>() // Eventos por data
    private var selectedDate: String = LocalDate.now().toString() // Data selecionada no formato "yyyy-MM-dd"

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        val rvEvents = findViewById<RecyclerView>(R.id.rvEvents)
        val btnAddEvent = findViewById<Button>(R.id.btnAddEvent)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_item1 -> {
                    // Handle item 1 click
                }
                R.id.nav_item2 -> {
                    // Handle item 2 click
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

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
    }

    // Atualiza os eventos da data selecionada
    private fun updateEventsForSelectedDate() {
        eventAdapter.updateEvents(eventMap[selectedDate] ?: mutableListOf())
    }

    private fun openEditEventActivity(position: Int) {
        val event = eventMap[selectedDate]?.get(position)
        val intent = Intent(this, EditEventActivity::class.java).apply {
            putExtra("event", event)
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
            val updatedEvent = result.data?.getStringExtra("updatedEvent")
            val position = result.data?.getIntExtra("position", -1)
            if (updatedEvent != null && position != null && position >= 0) {
                eventMap[selectedDate]?.set(position, updatedEvent)
                updateEventsForSelectedDate()
            }
        }
    }

    private val addEventLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val event = result.data?.getStringExtra("event")
            val date = result.data?.getStringExtra("date")
            if (event != null && date != null) {
                if (!eventMap.containsKey(date)) {
                    eventMap[date] = mutableListOf()
                }
                eventMap[date]?.add(event)
                updateEventsForSelectedDate()
            }
        }
    }
}
