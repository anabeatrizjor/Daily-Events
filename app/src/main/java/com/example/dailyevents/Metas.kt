package com.example.dailyevents

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Metas : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metas)

        val metasButton = findViewById<Button>(R.id.metasButton)
        val todosButton = findViewById<Button>(R.id.todosButton)
        val metasView = findViewById<ImageView>(R.id.viewPageMetas)
        val cess = findViewById<Button>(R.id.acessMeta)

        cess.setOnClickListener {
            startActivity(Intent(this, MetasSelecionar::class.java))
        }

        val webButton = findViewById<Button>(R.id.webButton)

        webButton.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java)
            startActivity(intent)
        }


        todosButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        metasButton.setOnClickListener {
            Toast.makeText(this, "Você já está na aba 'Todos'", Toast.LENGTH_SHORT).show()
        }

        window.statusBarColor = getColor(R.color.subColor)
    }
}