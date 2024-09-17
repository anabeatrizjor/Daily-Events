package com.example.dailyevents

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class AddEventActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val etTime = findViewById<EditText>(R.id.etTime)
        val btnSaveEvent = findViewById<Button>(R.id.btnSaveEvent)
        val backArrow = findViewById<ImageView>(R.id.backArrow)

        backArrow.setOnClickListener {
            finish()
        }

        etTime.setOnClickListener {
            showTimePickerDialog()
        }

        btnSaveEvent.setOnClickListener {
            val title = etTitle.text.toString()
            val description = etDescription.text.toString()
            val time = etTime.text.toString()

            if (title.isNotBlank() && time.isNotBlank()) {
                val resultIntent = Intent().apply {
                    putExtra("title", title)
                    putExtra("description", description)
                    putExtra("time", time)
                    putExtra("date", intent.getStringExtra("date"))
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "O título e o horário são obrigatórios", Toast.LENGTH_SHORT).show()
            }
        }

        window.statusBarColor = getColor(R.color.subColor)
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
