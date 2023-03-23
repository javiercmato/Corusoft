package com.corusoft.ticketmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
    }

    fun signUp(view: View) {
        Log.d("signUp", "Button sign up")
        // ToDo: username/email, password, ...
        val intent = Intent(this, Landing::class.java)
        startActivity(intent)
    }

    fun logIn(view: View) {
        Log.d("logIn", "Button log in")
        val intent = Intent(this, LogIn::class.java)
        startActivity(intent)
    }

}