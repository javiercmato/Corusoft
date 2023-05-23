package com.corusoft.ticketmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.corusoft.ticketmanager.adapter.TicketAdapter
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UserProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        datosCorrutina()
    }

    fun onClickContactsListButton(view: View) {
        Log.d("contactsListButton", "Clicked View Contacts button")
        Toast.makeText(applicationContext, "Clicked View Contacts button", Toast.LENGTH_SHORT)
            .show()

        val intent = Intent(this, UserProfile::class.java)
        startActivity(intent)
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
                recyclerView.adapter = TicketAdapter(this@UserProfile, myTickets)
                recyclerView.setHasFixedSize(true)
            }
        }
        println("Listado de tickets recibidos")
    }

    private fun fetchContactsCoroutine() = runBlocking {
        coroutineScope {
            launch {
                delay(2000L)
                println("Fetching contacts from backend...")
                Toast.makeText(applicationContext, "Fetching contacts", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        println("Received contacts list fron backend")
        Toast.makeText(applicationContext, "Fetch contacts completed", Toast.LENGTH_SHORT)
            .show()
    }



}