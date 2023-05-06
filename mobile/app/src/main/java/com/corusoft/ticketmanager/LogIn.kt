package com.corusoft.ticketmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.corusoft.ticketmanager.backend.BackendAPI
import com.corusoft.ticketmanager.backend.dtos.users.LoginParamsDTO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LogIn : AppCompatActivity() {
    val backend: BackendAPI = BackendAPI()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        Log.d("logIn", "Starting app")
    }

    /*
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

    */

    fun backendLogin(view: View) = runBlocking {
        val nickname = findViewById<EditText>(R.id.editTextTextPersonName3).text.toString()
        val password = findViewById<EditText>(R.id.editTextTextPersonName4).text.toString()

        try {
            coroutineScope {
                launch {
                    val params = LoginParamsDTO(nickname, password)
                    val response = backend.backendLogin(params)
                    System.err.println("TOKEN DEL USUARIO: ${response.serviceToken}")
                }
            }
        } catch (ex: Exception) {
            System.err.println(ex.localizedMessage)
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