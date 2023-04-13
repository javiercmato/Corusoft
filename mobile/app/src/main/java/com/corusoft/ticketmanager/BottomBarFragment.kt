package com.corusoft.ticketmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomBarFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflar el archivo de diseño del Fragment
        val view = inflater.inflate(R.layout.fragment_bottom_bar, container, false)
        // Configurar el BottomNavigationView u otros elementos aquí si es necesario
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener referencia al BottomNavigationView dentro del Fragment
        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    Log.d("BottomNavigation", "Pulsado botón Home")
                    val intent = Intent(requireActivity(), Landing::class.java)
                    startActivity(intent)
                }
                R.id.page_2 -> {
                    Log.d("BottomNavigation", "Pulsado botón Tickets")
                    val intent = Intent(requireActivity(), MyTickets::class.java)
                    startActivity(intent)
                }
                R.id.page_3 -> {
                    Log.d("BottomNavigation", "Pulsado botón Scan")
                    Log.d("BottomNavigation", "Abriendo cámara...")
                }
                R.id.page_4 -> {
                    Log.d("BottomNavigation", "Pulsado botón 4")
                    val intent = Intent(requireActivity(), AddTicket::class.java)
                    startActivity(intent)
                }
                R.id.page_5 -> {
                    Log.d("BottomNavigation", "Pulsado botón profile")
                    val intent = Intent(requireActivity(), UserProfile::class.java)
                    startActivity(intent)
                }
            }
            true
        }
    }

}
