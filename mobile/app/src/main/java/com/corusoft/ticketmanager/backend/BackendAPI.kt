package com.corusoft.ticketmanager.backend

import com.corusoft.ticketmanager.backend.dtos.common.GenericValueDTO
import com.corusoft.ticketmanager.backend.dtos.tickets.CategoryDTO
import com.corusoft.ticketmanager.backend.dtos.tickets.CreateCustomizedCategoryParamsDTO
import com.corusoft.ticketmanager.backend.dtos.tickets.CreateTicketParamsDTO
import com.corusoft.ticketmanager.backend.dtos.tickets.CustomizedCategoryDTO
import com.corusoft.ticketmanager.backend.dtos.tickets.ParsedTicketDTO
import com.corusoft.ticketmanager.backend.dtos.tickets.ShareTicketParamsDTO
import com.corusoft.ticketmanager.backend.dtos.tickets.TicketDTO
import com.corusoft.ticketmanager.backend.dtos.users.AuthenticatedUserDTO
import com.corusoft.ticketmanager.backend.dtos.users.LoginParamsDTO
import com.corusoft.ticketmanager.backend.dtos.users.RegisterUserParamsDTO
import com.corusoft.ticketmanager.backend.dtos.users.SubscriptionDTO
import com.corusoft.ticketmanager.backend.dtos.users.UserDTO
import com.corusoft.ticketmanager.backend.security.TokenInterceptor
import com.corusoft.ticketmanager.backend.security.TokenManager
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class BackendAPI {
    val BACKEND_URL: String = "http://10.0.2.2:8080/backend/api/"
    val tokenManager: TokenManager = TokenManager

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


    /* ******************** Funciones auxiliares ******************** */
    fun saveTokenInStorage(response: Response<AuthenticatedUserDTO>) {
        val token: String? = response.body()?.serviceToken
        tokenManager.saveToken(token)
    }

    /* ******************** Llamadas al backend ******************** */
    suspend fun login(params: LoginParamsDTO): UserDTO {
        val response = backendService.login(params)
        saveTokenInStorage(response)
        val responseBody = response.body()!!

        return responseBody.user
    }

    suspend fun signUp(params: RegisterUserParamsDTO): UserDTO {
        val response = backendService.register(params)
        saveTokenInStorage(response)
        val responseBody = response.body()!!

        return responseBody.user
    }

    suspend fun loginFromToken(): UserDTO {
        val response = backendService.loginFromToken()
        saveTokenInStorage(response)
        val responseBody = response.body()!!

        return responseBody.user
    }

    suspend fun subscribeToPremium(userID: Long): SubscriptionDTO {
        val response = backendService.subscribeToPremium(userID)
        val responseBody = response.body()!!

        return responseBody!!
    }

    suspend fun getAllCategories(): List<CategoryDTO> {
        val response = backendService.getAllCategories()
        val responseBody = response.body()

        return responseBody!!
    }

    suspend fun createCustomizedCategory(params: CreateCustomizedCategoryParamsDTO): CustomizedCategoryDTO {
        val response = backendService.createCustomizedCategory(params)
        val responseBody = response.body()

        return responseBody!!
    }

    suspend fun updateCustomizedCategory(categoryID: Long, params: GenericValueDTO<Float>): CustomizedCategoryDTO {
        val response = backendService.updateCustomizedCategory(categoryID, params)
        val responseBody = response.body()

        return responseBody!!
    }

    suspend fun getCustomizedCategoriesByUser(userID: Long) : List<CustomizedCategoryDTO> {
        val response = backendService.getCustomizedCategoriesByUser(userID)
        val responseBody = response.body()

        return responseBody!!
    }

    suspend fun parseTicket(params: GenericValueDTO<String>): ParsedTicketDTO {
        val response = backendService.parseTicket(params)
        val responseBody = response.body()

        return responseBody!!
    }

    suspend fun createTicket(params: CreateTicketParamsDTO): TicketDTO {
        val response = backendService.createTicket(params)
        val responseBody = response.body()

        return responseBody!!
    }

    suspend fun shareTicket(userID: Long, params: ShareTicketParamsDTO): TicketDTO {
        val response = backendService.shareTicket(userID, params)
        val responseBody = response.body()

        return responseBody!!
    }
}