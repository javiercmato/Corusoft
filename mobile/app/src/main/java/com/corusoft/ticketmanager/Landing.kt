package com.corusoft.ticketmanager

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.corusoft.ticketmanager.backend.BackendAPI
import com.corusoft.ticketmanager.backend.dtos.users.UserDTO
import com.corusoft.ticketmanager.backend.exceptions.BackendConnectionException
import com.corusoft.ticketmanager.backend.exceptions.BackendErrorException
import kotlinx.coroutines.launch


class Landing : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        requestForDashboardData()
    }

    /**
     * Solicita al backend los datos necesarios para la pantalla de inicio del usuario
     */
    private fun requestForDashboardData() {
        // Realizar peticiones al backend
        val backend = BackendAPI()
        var loggedUser: UserDTO?
        lifecycleScope.launch {
            try {
                loggedUser = backend.loginFromToken()
                Toast.makeText(
                    applicationContext,
                    "Usuario ${loggedUser?.nickname} logeado con token",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (ex: BackendErrorException) {
                Toast.makeText(
                    applicationContext,
                    ex.getDetails(),
                    Toast.LENGTH_SHORT
                ).show()
            } catch (ex: BackendConnectionException) {
                System.err.println(ex.message)
                Toast.makeText(
                    applicationContext,
                    ex.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}
