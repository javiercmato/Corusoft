package com.corusoft.ticketmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.LogPrinter
import android.view.View

class AddTicket : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ticket)
    }

    fun addTicket(view: View) {
        Log.d("addTicket", "Button add ticket")
        val intent = Intent(this, AddTicket::class.java )
        startActivity(intent);
    }


}