package com.corusoft.ticketmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.corusoft.ticketmanager.backend.BackendAPI
import com.corusoft.ticketmanager.backend.dtos.users.RegisterUserParamsDTO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SignUp : AppCompatActivity() {
    val backend: BackendAPI = BackendAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
    }

    fun backendSignUp(view: View) = runBlocking {
        val firstName = findViewById<EditText>(R.id.editTextTextPersonName9).text.toString()
        val lastName = findViewById<EditText>(R.id.editTextTextPersonName10).text.toString()
        val nickname = findViewById<EditText>(R.id.editTextTextPersonName5).text.toString()
        val email = findViewById<EditText>(R.id.editTextTextPersonName6).text.toString()
        val password = findViewById<EditText>(R.id.editTextTextPersonName7).text.toString()
        val repeatPassword = findViewById<EditText>(R.id.editTextTextPersonName8).text.toString()
        val params = RegisterUserParamsDTO(nickname, password, firstName + lastName, email)

        if (password == repeatPassword) {
            try {
                coroutineScope {
                    launch {
                        var success = false;
                        try {
                            val response = backend.signUp(params)
                            if (response != null) {
                                success = true
                                Log.i("SignUp", "Signup user '${response.nickname}' success")
                            }
                        } catch (ex: Exception) {
                            success = false
                        }

                        if (success) {
                            val intent = Intent(this@SignUp, Landing::class.java)
                            startActivity(intent)
                        }
                    }
                }
            } catch (ex: Exception) {
                System.err.println(ex.localizedMessage)
            }
        } else {
            Toast.makeText(this@SignUp, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
        }

    }

    fun logIn(view: View) {
        Log.d("logIn", "Button log in")
        val intent = Intent(this, LogIn::class.java)
        startActivity(intent)
    }

}