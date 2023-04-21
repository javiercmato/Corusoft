package com.corusoft.ticketmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*
import android.widget.Toast

class MyTickets : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_tickets)

        datosCorrutina()

        //Obtener referencia al botón
        val filterButton = findViewById<Button>(R.id.buttonFilter)

        filterButton.setOnClickListener {
            Log.d("MyTicketsActivity", "Se pulsó el botón para filtrar los tickets")
        }

        //Botón para ir a la pantalla de filtro
        filterButton.setOnClickListener {
            val intent = Intent(this, MyTicketsFilter::class.java)
            startActivity(intent)
        }
    }

    private fun datosCorrutina() = runBlocking {
        coroutineScope {
            launch {
                delay(1000L)
                println("Solicitando listado de tickets...")
            }
        }
        println("Listado de tickets recibidos")
    }
}