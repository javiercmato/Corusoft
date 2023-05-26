package com.corusoft.ticketmanager.backend.security

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val SERVICE_TOKEN_NAME: String = "ServiceToken"
    private lateinit var sharedPreferences: SharedPreferences
    const val JWT_KEY: String = "jwt"


    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(SERVICE_TOKEN_NAME, Context.MODE_PRIVATE)
    }


    fun saveToken(jwt: String) {
        println("Storing $JWT_KEY")
        sharedPreferences.edit()
            .putString(JWT_KEY, jwt)
            .apply()
    }

    fun removeToken(key: String? = JWT_KEY) {
        println("Removing $key")
        sharedPreferences.edit()
            .remove(key)
            .apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(JWT_KEY, null)
    }
}
