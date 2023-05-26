package com.corusoft.ticketmanager

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.corusoft.ticketmanager.backend.BackendAPI
import com.corusoft.ticketmanager.backend.dtos.users.LoginParamsDTO
import com.corusoft.ticketmanager.backend.dtos.users.UserDTO
import com.corusoft.ticketmanager.backend.exceptions.BackendConnectionException
import com.corusoft.ticketmanager.backend.exceptions.BackendErrorException
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class LogIn : AppCompatActivity(), View.OnClickListener {

    // instance variable
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    // constant necessary for log in
    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val googleButton = findViewById<SignInButton>(R.id.sign_in_button)
        googleButton.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            goToMainActivity()
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.sign_in_button) {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            /************/
            // To check that the user information is being collected properly
            val account = task.getResult(ApiException::class.java)!!
            Log.d(TAG, "firebaseAuthWithGoogle:" + account.displayName)
            /************/
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Log in went well
            Toast.makeText(this, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show()
            goToMainActivity()
        } catch (e: ApiException) {
            // Log in failed
            Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, Landing::class.java)
        startActivity(intent)
        finish()
    }


    fun backendLogin(view: View) {
        val nickname = findViewById<EditText>(R.id.editTextTextPersonName3).text.toString()
        val password = findViewById<EditText>(R.id.editTextTextPersonName4).text.toString()
        val params = LoginParamsDTO(nickname, password)

        // Realizar petición a backend
        val backend = BackendAPI()
        var response: UserDTO?
        lifecycleScope.launch {
            try {
                response = backend.login(params)
                Toast.makeText(
                    applicationContext,
                    "Usuario ${response?.nickname} logeado",
                    Toast.LENGTH_SHORT
                )
                    .show()

                val intent = Intent(this@LogIn, Landing::class.java)
                startActivity(intent)
            } catch (ex: BackendErrorException) {
                Toast.makeText(applicationContext, ex.getDetails(), Toast.LENGTH_SHORT)
                    .show()
            } catch (ex: BackendConnectionException) {
                System.err.println(ex.message)
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT)
                    .show()
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
