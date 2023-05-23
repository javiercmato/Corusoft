package com.corusoft.ticketmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.corusoft.ticketmanager.backend.BackendAPI
import com.corusoft.ticketmanager.backend.dtos.users.RegisterUserParamsDTO
import com.corusoft.ticketmanager.backend.dtos.users.UserDTO
import com.corusoft.ticketmanager.backend.exceptions.BackendConnectionException
import com.corusoft.ticketmanager.backend.exceptions.BackendErrorException
import kotlinx.coroutines.launch

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
    }

    fun backendSignUp(view: View) {
        val firstName = findViewById<EditText>(R.id.editTextTextPersonName9).text.toString()
        val lastName = findViewById<EditText>(R.id.editTextTextPersonName10).text.toString()
        val nickname = findViewById<EditText>(R.id.editTextTextPersonName5).text.toString()
        val email = findViewById<EditText>(R.id.editTextTextPersonName6).text.toString()
        val password = findViewById<EditText>(R.id.editTextTextPersonName7).text.toString()
        val params = RegisterUserParamsDTO(nickname, password, "$firstName $lastName", email)

        // Petición al backend
        val backend = BackendAPI()
        var response: UserDTO?
        lifecycleScope.launch {
            try {
                response = backend.signUp(params)
                println("SignUp.kt ha recibido " + response.toString())
                Toast.makeText(
                    applicationContext,
                    "Usuario ${response?.nickname} registrado",
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(this@SignUp, Landing::class.java)
                startActivity(intent)
            } catch (ex: BackendErrorException) {
                Toast.makeText(
                    applicationContext,
                    ex.getDetails(),
                    Toast.LENGTH_LONG
                ).show()
            } catch (ex: BackendConnectionException) {
                Log.d("SignUp", ex.message.toString())
                Toast.makeText(
                    applicationContext,
                    "Error: " + ex.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}
