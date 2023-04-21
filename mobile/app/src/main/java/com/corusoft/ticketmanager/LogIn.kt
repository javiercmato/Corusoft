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

class LogIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
    }

    fun logIn(view: View) = runBlocking {
        Log.d("logIn", "Button log in")
        // ToDo: username/email and password
        //val intent = Intent(this, Landing::class.java)
        //startActivity(intent)
        coroutineScope {
            launch {
                // val jsonBody = "{ username: \"$username\", token: \"$token\" }"
                val result = try {
                    //login
                    delay(1000L)
                } catch (e: Exception) {
                    Exception("Network request failed")
                }
                if (true) { //cambiar por when (Result)
                    val intent = Intent(this@LogIn, Landing::class.java)
                    startActivity(intent)
                } else {
                    //Error
                }
            }
        }
    }

    fun logInGoogle(view: View) = runBlocking {
        Log.d("logInGoogle", "Button log in google")
        // ToDo: username/email and password
        //val intent = Intent(this, Landing::class.java)
        //startActivity(intent)
        coroutineScope {
            launch {
                // val jsonBody = "{ username: \"$username\", token: \"$token\" }"
                val result = try {
                    //login
                    delay(1000L)
                } catch (e: Exception) {
                    Exception("Network request failed")
                }
                if (true) { //cambiar por when (Result)
                    val intent = Intent(this@LogIn, Landing::class.java)
                    startActivity(intent)
                } else {
                    //Error
                }
            }
        }
    }

    fun signUp(view: View) {
        Log.d("signUp", "Button sign up")
        val intent = Intent(this, SignUp::class.java)
        startActivity(intent)
    }

}