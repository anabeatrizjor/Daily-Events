package com.example.dailyevents

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class EditEventActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_event)

        val etTitle = findViewById<EditText>(R.id.etTitleEdit)
        val etDescription = findViewById<EditText>(R.id.etDescriptionEdit)
        val etTime = findViewById<EditText>(R.id.etTimeEdit)
        val btnSaveEvent = findViewById<Button>(R.id.btnSaveEditEvent)
        val backArrow = findViewById<ImageView>(R.id.backArrowEdit)

        backArrow.setOnClickListener {
            finish()
        }

        // Recebe dados da intent
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val time = intent.getStringExtra("time")
        val position = intent.getIntExtra("position", -1)

        etTitle.setText(title)
        etDescription.setText(description)
        etTime.setText(time)

        btnSaveEvent.setOnClickListener {
            val updatedTitle = etTitle.text.toString()
            val updatedDescription = etDescription.text.toString()
            val updatedTime = etTime.text.toString()

            val resultIntent = Intent().apply {
                putExtra("updatedTitle", updatedTitle)
                putExtra("updatedDescription", updatedDescription)
                putExtra("updatedTime", updatedTime)
                putExtra("position", position)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        window.statusBarColor = getColor(R.color.subColor)
    }
}
