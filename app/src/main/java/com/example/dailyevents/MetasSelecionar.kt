package com.example.dailyevents

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MetasSelecionar : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metas_selecionar)

        val confirmButton = findViewById<Button>(R.id.buttonConfirmar)

        confirmButton.setOnClickListener {
            startActivity(Intent(this, Metas::class.java))
        }

        window.statusBarColor = getColor(R.color.subColor)

    }
}