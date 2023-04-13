package com.corusoft.ticketmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView

class Landing : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        // Obtener referencia al botón
        val button = findViewById<FloatingActionButton>(R.id.floatingActionButton2)

        // Añadir listener al botón
        button.setOnClickListener {
            // Mostrar mensaje de log
            Log.d("LandingActivity", "Se pulsó el botón para agregar un ticket")
        }

    }
}
