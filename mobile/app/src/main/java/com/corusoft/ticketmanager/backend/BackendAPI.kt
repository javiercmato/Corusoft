package com.corusoft.ticketmanager.backend

import com.corusoft.ticketmanager.backend.dtos.users.AuthenticatedUserDTO
import com.corusoft.ticketmanager.backend.dtos.users.LoginParamsDTO
import com.corusoft.ticketmanager.backend.security.TokenInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class BackendAPI {
    val BACKEND_URL: String = "http://10.0.2.2:8080/backend/api/"

    // Añadir interceptor para agregar token a peticiones
    val httpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor)
        .build();

    // Crear instancia de Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BACKEND_URL)
        .addConverterFactory(JacksonConverterFactory.create())
        .client(httpClient)
        .build()

    val backendService = retrofit.create(IBackendService::class.java)


    /* ******************** Llamadas al backend ******************** */
    suspend fun backendLogin(params: LoginParamsDTO): AuthenticatedUserDTO {
        val response = backendService.login(params)
        val responseBody = response?.body()!!

        return AuthenticatedUserDTO(responseBody.serviceToken, responseBody.user)
    }
}