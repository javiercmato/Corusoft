package com.corusoft.ticketmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
    }

    fun signUp(view: View) = runBlocking {
        Log.d("signUp", "Button sign up")
        // ToDo: username/email, password, ...
        //val intent = Intent(this, Landing::class.java)
        //startActivity(intent)
        coroutineScope {
            launch {
                // val jsonBody = "{ username: \"$username\", token: \"$token\", ... }"
                val result = try {
                    //signUp
                    delay(1000L)
                } catch (e: Exception) {
                    Exception("Network request failed")
                }
                if (true) { //cambiar por when (Result)
                    val intent = Intent(this@SignUp, Landing::class.java)
                    startActivity(intent)
                } else {
                    //Error
                }
            }
        }
    }

    fun logIn(view: View) {
        Log.d("logIn", "Button log in")
        val intent = Intent(this, LogIn::class.java)
        startActivity(intent)
    }

}