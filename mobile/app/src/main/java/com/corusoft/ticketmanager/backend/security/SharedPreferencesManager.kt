package com.corusoft.ticketmanager.backend.security

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesManager {
    private const val SHARED_PREFERENCES_MANAGER_NAME = "TicketManagerSharedPreferencesManager"
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.applicationContext
            .getSharedPreferences(SHARED_PREFERENCES_MANAGER_NAME, Context.MODE_PRIVATE)
    }
}