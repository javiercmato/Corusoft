package com.corusoft.ticketmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UserProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
    }

    fun onClickContactsListButton(view: View) {
        Log.d("contactsListButton", "Clicked View Contacts button")
        Toast.makeText(applicationContext, "Clicked View Contacts button", Toast.LENGTH_SHORT)
            .show()

        val intent = Intent(this, UserProfile::class.java)
        startActivity(intent)
    }

}