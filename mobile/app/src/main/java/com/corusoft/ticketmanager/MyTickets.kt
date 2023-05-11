package com.corusoft.ticketmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.corusoft.ticketmanager.adapter.TicketAdapter
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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
                // Http a Backend
                val myTickets = TicketDataSource().loadTickets()
                Toast.makeText(applicationContext, myTickets.size.toString(), Toast.LENGTH_SHORT)
                    .show()
                val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
                recyclerView.adapter = TicketAdapter(this@MyTickets, myTickets)
                recyclerView.setHasFixedSize(true)
            }
        }
        println("Listado de tickets recibidos")
    }
}