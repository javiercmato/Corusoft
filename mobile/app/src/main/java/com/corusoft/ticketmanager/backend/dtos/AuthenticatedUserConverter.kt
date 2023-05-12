package com.corusoft.ticketmanager.backend.dtos

import com.corusoft.ticketmanager.backend.dtos.users.AuthenticatedUserDTO
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody
import retrofit2.Converter

class AuthenticatedUserConverter(private val moshi: Moshi) : Converter<ResponseBody, AuthenticatedUserDTO> {
    override fun convert(value: ResponseBody): AuthenticatedUserDTO? {
        val adapter = moshi.adapter(AuthenticatedUserDTO::class.java)
        return adapter.fromJson(value.string())
    }
}