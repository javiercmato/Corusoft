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
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Añadir listener al botón
        button.setOnClickListener {
            // Mostrar mensaje de log
            Log.d("LandingActivity", "Se pulsó el botón para agregar un ticket")
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    Log.d("BottomNavigation", "Pulsado botón Home")
                    val intent = Intent(this, Landing::class.java)
                    startActivity(intent)
                }
                R.id.page_2 -> {
                    Log.d("BottomNavigation", "Pulsado botón Tickets")
                    val intent = Intent(this, MyTickets::class.java)
                    startActivity(intent)
                }
                R.id.page_3 -> {
                    Log.d("BottomNavigation", "Pulsado botón Scan")
                    Log.d("BottomNavigation", "Abriendo cámara...")
                }
                R.id.page_4 -> {
                    Log.d("BottomNavigation", "Pulsado botón 4")
                }
                R.id.page_5 -> {
                    Log.d("BottomNavigation", "Pulsado botón profile")
                    val intent = Intent(this, UserProfile::class.java)
                    startActivity(intent)
                }
            }
            true
        }
    }
}
