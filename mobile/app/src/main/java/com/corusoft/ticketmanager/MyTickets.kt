package com.corusoft.ticketmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.util.Log
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView

class MyTickets : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_tickets)

        //Obtener referencia al botón
        val addButton = findViewById<FloatingActionButton>(R.id.buttonAdd)
        val filterButton = findViewById<Button>(R.id.buttonFilter)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        //Añadir listener al botón
        addButton.setOnClickListener {
            //Mostrar mensaje de log
            Log.d("MyTicketsActivity", "Se pulsó el botón para agregar un ticket")
        }

        filterButton.setOnClickListener {
            Log.d("MyTicketsActivity", "Se pulsó el botón para filtrar los tickets")
        }

        //Botón para ir a la pantalla de filtro
        filterButton.setOnClickListener {
            val intent = Intent(this, MyTicketsFilter::class.java)
            startActivity(intent)
        }

        //Menu inferior
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