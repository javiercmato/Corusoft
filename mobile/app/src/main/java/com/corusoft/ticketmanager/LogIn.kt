package com.corusoft.ticketmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class LogIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
    }

    fun logIn(view: View) {
        Log.d("logIn", "Button log in")
        // ToDo: username/email and password
        val intent = Intent(this, Landing::class.java)
        startActivity(intent)
    }

    fun logInGoogle(view: View) {
        Log.d("logInGoogle", "Button log in google")
        // ToDo: username/email and password
        val intent = Intent(this, Landing::class.java)
        startActivity(intent)
    }

    fun signUp(view: View) {
        Log.d("signUp", "Button sign up")
        val intent = Intent(this, SignUp::class.java)
        startActivity(intent)
    }

}