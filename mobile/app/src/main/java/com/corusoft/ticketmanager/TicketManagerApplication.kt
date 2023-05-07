package com.corusoft.ticketmanager

import android.app.Application
import com.corusoft.ticketmanager.backend.dtos.LocalDateTimeDeserializer
import com.corusoft.ticketmanager.backend.security.TokenManager
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import java.time.LocalDateTime

class TicketManagerApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        TokenManager.init(this)

        setupDateTimeDeserializer()
    }

    private fun setupDateTimeDeserializer() {
        var module = SimpleModule()
        module.addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer())

        val objectMapper = ObjectMapper()
        objectMapper.registerModule(module)
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }
}