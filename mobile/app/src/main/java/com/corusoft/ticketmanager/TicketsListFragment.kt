package com.corusoft.ticketmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class TicketsListFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflar el archivo de diseño del Fragment
        val view = inflater.inflate(R.layout.fragment_tickets_list, container, false)
        // Configurar el elementos aquí si es necesario
        return view
    }
}