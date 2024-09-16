package com.example.dailyevents

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class EditEventActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_event)

        val etEvent = findViewById<EditText>(R.id.etEditEvent)
        val btnSaveEvent = findViewById<Button>(R.id.btnSaveEditEvent)

        // Recebe dados da intent
        val event = intent.getStringExtra("event")
        val position = intent.getIntExtra("position", -1)
        etEvent.setText(event)

        btnSaveEvent.setOnClickListener {
            val updatedEvent = etEvent.text.toString()
            val resultIntent = Intent().apply {
                putExtra("updatedEvent", updatedEvent)
                putExtra("position", position)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}
