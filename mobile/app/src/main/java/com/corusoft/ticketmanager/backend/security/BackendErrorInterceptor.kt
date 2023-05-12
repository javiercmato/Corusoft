package com.corusoft.ticketmanager.backend.security

import com.corusoft.ticketmanager.backend.BackendErrorManager
import com.corusoft.ticketmanager.backend.dtos.common.GlobalError
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.Interceptor
import okhttp3.Response


object BackendErrorInterceptor : Interceptor {
    private val objectMapper = ObjectMapper()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (!response.isSuccessful) {
            val errorBody = response.body()?.string()
            val errorResponse = objectMapper.readValue(errorBody, GlobalError::class.java)
            BackendErrorManager.setGlobalError(errorResponse)
        }

        return response
    }
}

