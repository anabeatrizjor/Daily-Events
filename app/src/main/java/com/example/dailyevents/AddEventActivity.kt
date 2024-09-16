package com.example.dailyevents

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class AddEventActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val etTime = findViewById<EditText>(R.id.etTime)
        val btnSaveEvent = findViewById<Button>(R.id.btnSaveEvent)

        etTime.setOnClickListener {
            showTimePickerDialog()
        }

        btnSaveEvent.setOnClickListener {
            val title = etTitle.text.toString()
            val description = etDescription.text.toString()
            val time = etTime.text.toString()
            if (title.isNotBlank() && time.isNotBlank()) {
                val event = "$title\n$description\n$time"
                val resultIntent = Intent().apply {
                    putExtra("event", event)
                    putExtra("date", intent.getStringExtra("date"))
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                // Handle empty title or time case if needed
            }
        }
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val timeString = String.format("%02d:%02d", selectedHour, selectedMinute)
            findViewById<EditText>(R.id.etTime).setText(timeString)
        }, hour, minute, true)
        timePicker.show()
    }
}
