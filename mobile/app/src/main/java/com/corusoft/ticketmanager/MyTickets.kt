package com.corusoft.ticketmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.corusoft.ticketmanager.adapter.TicketAdapter
import com.corusoft.ticketmanager.backend.BackendAPI
import com.corusoft.ticketmanager.backend.dtos.tickets.TicketDTO
import com.corusoft.ticketmanager.backend.dtos.tickets.filters.TicketFilterParamsDTO
import com.corusoft.ticketmanager.backend.exceptions.BackendConnectionException
import com.corusoft.ticketmanager.backend.exceptions.BackendErrorException
import kotlinx.coroutines.launch
import java.util.Collections

class MyTickets : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_tickets)

        fetchTickets()

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


    private fun fetchTickets() {
        lifecycleScope.launch {
            println("Solicitando listado de tickets...")
            // Http a Backend
            val backend = BackendAPI()
            var myTickets: List<TicketDTO> = Collections.emptyList()
            try {
                var filterParams: TicketFilterParamsDTO = TicketFilterParamsDTO.TicketFilterParamsDTOBuilder().build()
                myTickets = backend.filterUserTicketsByCriteria(2, filterParams)
            } catch (ex: BackendErrorException) {
                Toast.makeText(applicationContext, ex.getDetails(), Toast.LENGTH_SHORT)
                    .show()
            } catch (ex: BackendConnectionException) {
                System.err.println(ex.message)
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT)
                    .show()
            }


            Toast.makeText(applicationContext, myTickets.size.toString(), Toast.LENGTH_SHORT).show()
            val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
            recyclerView.adapter = TicketAdapter(this@MyTickets, myTickets)
            recyclerView.setHasFixedSize(true)
        }
        println("Listado de tickets recibidos")
    }
}
