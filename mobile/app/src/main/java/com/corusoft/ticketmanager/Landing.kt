package com.corusoft.ticketmanager

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class Landing : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        datosCorrutina()

    }

    private fun datosCorrutina() = runBlocking {
        coroutineScope {
            launch {
                delay(1000L)
                println("Solicitando datos históricos a backend...")
            }
        }
        println("Datos históricos recibidos")
        Toast.makeText(applicationContext, "Datos históricos recibidos", Toast.LENGTH_SHORT)
            .show()
    }

}
