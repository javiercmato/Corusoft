package com.corusoft.ticketmanager.backend

import com.corusoft.ticketmanager.backend.dtos.common.ErrorsDTO
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
import com.corusoft.ticketmanager.backend.exceptions.BackendConnectionException
import com.corusoft.ticketmanager.backend.exceptions.BackendErrorException
import com.corusoft.ticketmanager.backend.exceptions.GlobalErrorException
import com.corusoft.ticketmanager.backend.security.TokenInterceptor
import com.corusoft.ticketmanager.backend.security.TokenManager
import com.fasterxml.jackson.databind.ObjectMapper
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.util.concurrent.atomic.AtomicReference

class BackendAPI {
    val BACKEND_URL: String = "http://10.0.2.2:8080/backend/api/"
    private val retrofitInstance: Retrofit
    private val tokenManager: TokenManager = TokenManager
    private val jsonMapper = ObjectMapper()


    init {
        // Añadir interceptor para agregar token a peticiones
         val httpClient: OkHttpClient = OkHttpClient.Builder()
             .addInterceptor(TokenInterceptor)
             .build()

        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()


        // Crear instancia de Retrofit
        retrofitInstance = Retrofit.Builder()
            .baseUrl(BACKEND_URL)
            //.addConverterFactory(JacksonConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(httpClient)
            .build()
    }

    private fun getInstance(): IBackendService = retrofitInstance.create(IBackendService::class.java)


    /* ******************** Funciones auxiliares ******************** */
    private fun saveTokenInStorage(token: String) {
        tokenManager.saveToken(token)
    }

    private fun removeTokenFromStorage() {
        tokenManager.removeToken(tokenManager.JWT_KEY)
    }

    @Throws(BackendErrorException::class)
    private fun <T> handleError(response: Response<T>) {
        val errorBody = response.errorBody()!!
        val errorCause = jsonMapper.readValue(errorBody.string(), ErrorsDTO::class.java)

        throw BackendErrorException(errorCause)
    }


    /* ******************** Llamadas al backend ******************** */
    /* ---------- USERS ---------- */
    @Throws(BackendErrorException::class, BackendConnectionException::class)
    suspend fun login(params: LoginParamsDTO): UserDTO {
        val service = getInstance()
        val response : Response<AuthenticatedUserDTO>
        val result = AtomicReference<UserDTO>()

        try {
            response = service.login(params)
        } catch (ex: Exception) {
            throw BackendConnectionException()
        }

        if (response.isSuccessful) {
            val body = response.body()!!
            body.serviceToken?.let { saveTokenInStorage(it) }
            result.set(body.user)
        } else {
            handleError(response)
        }

        return result.get()
    }


    @Throws(BackendErrorException::class, BackendConnectionException::class)
    suspend fun signUp(params: RegisterUserParamsDTO): UserDTO {
        val service = getInstance()
        val response : Response<AuthenticatedUserDTO>
        val result = AtomicReference<UserDTO>()

        try {
            response = service.register(params)
        } catch (ex: Exception) {
            throw BackendConnectionException()
        }

        if (response.isSuccessful) {
            val body = response.body()!!
            body.serviceToken?.let { saveTokenInStorage(it) }
            result.set(body.user)
        } else {
            handleError(response)
        }

        return result.get()
    }

    @Throws(BackendErrorException::class, BackendConnectionException::class)
    suspend fun loginFromToken(): UserDTO {
        val service = getInstance()
        val response : Response<AuthenticatedUserDTO>
        val result = AtomicReference<UserDTO>()

        try {
            response = service.loginFromToken()
        } catch (ex: IOException) {
            throw BackendConnectionException()
        }

        if (response.isSuccessful) {
            val body = response.body()!!
            body.serviceToken?.let { saveTokenInStorage(it) }
            result.set(body.user)
        } else {
            handleError(response)
        }

        return result.get()
    }

    @Throws(GlobalErrorException::class)
    suspend fun logout() {
        try {
            removeTokenFromStorage()
        } catch (ex: Exception) {
            throw GlobalErrorException("Ha ocurrido un problema inesperado")
        }
    }

    @Throws(BackendErrorException::class, BackendConnectionException::class)
    suspend fun subscribeToPremium(userID: Long): SubscriptionDTO {
        val service = getInstance()
        val response : Response<SubscriptionDTO>
        val result = AtomicReference<SubscriptionDTO>()

        try {
            response = service.subscribeToPremium(userID)
        } catch (ex: IOException) {
            throw BackendConnectionException()
        }

        if (response.isSuccessful) {
            val body = response.body()!!
            result.set(body)
        } else {
            handleError(response)
        }

        return result.get()
    }


    /* ---------- TICKETS ---------- */
    @Throws(BackendErrorException::class, BackendConnectionException::class)
    suspend fun getAllCategories(): List<CategoryDTO> {
        val service = getInstance()
        val response : Response<List<CategoryDTO>>
        val result = AtomicReference<List<CategoryDTO>>()

        try {
            response = service.getAllCategories()
        } catch (ex: IOException) {
            throw BackendConnectionException()
        }

        if (response.isSuccessful) {
            val body = response.body()!!
            result.set(body)
        } else {
            handleError(response)
        }

        return result.get()
    }

    @Throws(BackendErrorException::class, BackendConnectionException::class)
    suspend fun createCustomizedCategory(params: CreateCustomizedCategoryParamsDTO): CustomizedCategoryDTO {
        val service = getInstance()
        val response : Response<CustomizedCategoryDTO>
        val result = AtomicReference<CustomizedCategoryDTO>()

        try {
            response = service.createCustomizedCategory(params)
        } catch (ex: IOException) {
            throw BackendConnectionException()
        }

        if (response.isSuccessful) {
            val body = response.body()!!
            result.set(body)
        } else {
            handleError(response)
        }

        return result.get()
    }

    @Throws(BackendErrorException::class, BackendConnectionException::class)
    suspend fun updateCustomizedCategory(categoryID: Long, params: GenericValueDTO<Float>): CustomizedCategoryDTO {
        val service = getInstance()
        val response : Response<CustomizedCategoryDTO>
        val result = AtomicReference<CustomizedCategoryDTO>()

        try {
            response = service.updateCustomizedCategory(categoryID, params)
        } catch (ex: IOException) {
            throw BackendConnectionException()
        }

        if (response.isSuccessful) {
            val body = response.body()!!
            result.set(body)
        } else {
            handleError(response)
        }

        return result.get()
    }

    @Throws(BackendErrorException::class, BackendConnectionException::class)
    suspend fun getCustomizedCategoriesByUser(userID: Long) : List<CustomizedCategoryDTO> {
        val service = getInstance()
        val response : Response<List<CustomizedCategoryDTO>>
        val result = AtomicReference<List<CustomizedCategoryDTO>>()

        try {
            response = service.getCustomizedCategoriesByUser(userID)
        } catch (ex: IOException) {
            throw BackendConnectionException()
        }

        if (response.isSuccessful) {
            val body = response.body()!!
            result.set(body)
        } else {
            handleError(response)
        }

        return result.get()
    }

    @Throws(BackendErrorException::class, BackendConnectionException::class)
    suspend fun parseTicket(params: GenericValueDTO<String>): ParsedTicketDTO {
        val service = getInstance()
        val response : Response<ParsedTicketDTO>
        val result = AtomicReference<ParsedTicketDTO>()

        try {
            response = service.parseTicket(params)
        } catch (ex: IOException) {
            throw BackendConnectionException()
        }

        if (response.isSuccessful) {
            val body = response.body()!!
            result.set(body)
        } else {
            handleError(response)
        }

        return result.get()
    }

    @Throws(BackendErrorException::class, BackendConnectionException::class)
    suspend fun createTicket(params: CreateTicketParamsDTO): TicketDTO {
        val service = getInstance()
        val response : Response<TicketDTO>
        val result = AtomicReference<TicketDTO>()

        try {
            response = service.createTicket(params)
        } catch (ex: IOException) {
            throw BackendConnectionException()
        }

        if (response.isSuccessful) {
            val body = response.body()!!
            result.set(body)
        } else {
            handleError(response)
        }

        return result.get()
    }

    @Throws(BackendErrorException::class, BackendConnectionException::class)
    suspend fun shareTicket(userID: Long, params: ShareTicketParamsDTO): TicketDTO {
        val service = getInstance()
        val response : Response<TicketDTO>
        val result = AtomicReference<TicketDTO>()

        try {
            response = service.shareTicket(userID, params)
        } catch (ex: IOException) {
            throw BackendConnectionException()
        }

        if (response.isSuccessful) {
            val body = response.body()!!
            result.set(body)
        } else {
            handleError(response)
        }

        return result.get()
    }

    // TODO: ACABAR DE IMPLEMENTAR LAS LLAMADAS A LOS ENDPOINTS DE AQUÍ ABAJO
    @Throws(BackendErrorException::class, BackendConnectionException::class)
    suspend fun deleteTicket(): Unit {}

    @Throws(BackendErrorException::class, BackendConnectionException::class)
    suspend fun getTicketDetails(): Unit {}

    @Throws(BackendErrorException::class, BackendConnectionException::class)
    suspend fun filterUserTicketsByCriteria(): Unit {}

    @Throws(BackendErrorException::class, BackendConnectionException::class)
    suspend fun getSharedTickets(): Unit {}


    /* ---------- STATS ---------- */
    @Throws(BackendErrorException::class, BackendConnectionException::class)
    suspend fun getSpendingsPerMonth(): Unit {}

    @Throws(BackendErrorException::class, BackendConnectionException::class)
    suspend fun getWastesPerCategory(): Unit {}

    @Throws(BackendErrorException::class, BackendConnectionException::class)
    suspend fun getSpendingsThisMonth(): Unit {}

    @Throws(BackendErrorException::class, BackendConnectionException::class)
    suspend fun getPercentagePerCategoryThisMonth(): Unit {}

}