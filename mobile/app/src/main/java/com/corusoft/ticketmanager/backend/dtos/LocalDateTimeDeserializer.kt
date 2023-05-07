package com.corusoft.ticketmanager.backend.dtos

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class LocalDateTimeDeserializer : JsonDeserializer<LocalDateTime>() {
    override fun deserialize(parser: JsonParser, context: DeserializationContext): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        val text = parser.text
        return LocalDateTime.parse(text, formatter)
    }
}