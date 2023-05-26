package com.corusoft.ticketmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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

class UserProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        datosCorrutina()
    }

    fun onClickLogOut(view: View) {
        val backend = BackendAPI()
        lifecycleScope.launch {
            backend.logout()
            val intent = Intent(this@UserProfile, LogIn::class.java)
            startActivity(intent)
        }
    }

    fun onClickContactsListButton(view: View) {
        Log.d("contactsListButton", "Clicked View Contacts button")
        Toast.makeText(applicationContext, "Clicked View Contacts button", Toast.LENGTH_SHORT)
            .show()

        val intent = Intent(this, UserProfile::class.java)
        startActivity(intent)
    }

    private fun datosCorrutina() {
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


            Toast.makeText(applicationContext, myTickets.size, Toast.LENGTH_SHORT)
                .show()
            val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
            recyclerView.adapter = TicketAdapter(this@UserProfile, myTickets)
            recyclerView.setHasFixedSize(true)
        }
        println("Listado de tickets recibidos")
    }

    private fun fetchTickets(): List<TicketDTO> {
        // Realizar petición a backend
        val backend = BackendAPI()
        var response: List<TicketDTO> = Collections.emptyList()
        lifecycleScope.launch {
            try {
                var filterParams: TicketFilterParamsDTO = TicketFilterParamsDTO.TicketFilterParamsDTOBuilder().build()
                response = backend.filterUserTicketsByCriteria(2, filterParams)
            } catch (ex: BackendErrorException) {
                Toast.makeText(applicationContext, ex.getDetails(), Toast.LENGTH_SHORT)
                    .show()
            } catch (ex: BackendConnectionException) {
                System.err.println(ex.message)
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return response
    }
}
