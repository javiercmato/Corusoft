package com.corusoft.ticketmanager.backend.security

import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response

object TokenInterceptor : Interceptor {
    private val tokenManager: TokenManager = TokenManager

    override fun intercept(chain: Interceptor.Chain): Response {
        var originalRequest = chain.request()

        // Obtener el token
        var bearerToken = ""
        if (tokenManager.getToken() != null) {
            bearerToken = "Bearer ${tokenManager.getToken()}"
        }

        // Generar petición con los headers
        val requestHeaders: Headers = Headers.Builder()
            .add("Authorization", bearerToken)
            .build()
        val requestWithAuthentication = originalRequest.newBuilder()
            .headers(requestHeaders)
            .build()

        return chain.proceed(requestWithAuthentication);
    }
}