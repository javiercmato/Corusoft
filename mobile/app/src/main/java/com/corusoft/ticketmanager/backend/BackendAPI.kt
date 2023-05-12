package com.corusoft.ticketmanager.backend

import com.corusoft.ticketmanager.backend.dtos.common.GenericValueDTO
import com.corusoft.ticketmanager.backend.dtos.common.GlobalError
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
    val retrofitInstance: Retrofit
    var errorManager = BackendErrorManager
    private val tokenManager: TokenManager = TokenManager
    private val jsonMapper = ObjectMapper()


    init {
        // Añadir interceptor para agregar token a peticiones
         val httpClient: OkHttpClient = OkHttpClient.Builder()
             .addInterceptor(TokenInterceptor)
             //.addInterceptor(BackendErrorInterceptor)
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



    /* ******************** Llamadas al backend ******************** */
    @Throws(GlobalErrorException::class)
    suspend fun login(params: LoginParamsDTO): UserDTO? {
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
            result.set(body.user)
        } else {
            val errorBody = response.errorBody()!!
            val errorCause = jsonMapper.readValue(errorBody.string(), GlobalError::class.java)

            throw GlobalErrorException(errorCause.globalError.toString())
        }

        return result.get()
    }

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
            result.set(body.user)
        } else {
            val errorBody = response.errorBody()!!
            val errorCause = jsonMapper.readValue(errorBody.string(), GlobalError::class.java)

            throw GlobalErrorException(errorCause.globalError.toString())
        }

        return result.get()
    }

    suspend fun loginFromToken(): UserDTO {
        val result = AtomicReference<UserDTO>()

        try {
            val backendResponse = getInstance().loginFromToken()

            if (backendResponse.isSuccessful) {
                val body = backendResponse.body()!!
                body.serviceToken?.let { saveTokenInStorage(it) }
                result.set(body.user)
            } else {
                val globalError = jsonMapper.readValue(backendResponse.toString(), GlobalError::class.java)
                errorManager.setGlobalError(globalError)

                throw GlobalErrorException(globalError.globalError)
            }
        } catch (ex: IOException) {
            throw BackendConnectionException()
        }

        return result.get()
    }

    suspend fun subscribeToPremium(userID: Long): SubscriptionDTO {
        val response = getInstance().subscribeToPremium(userID)
        val responseBody = response.body()!!

        return responseBody
    }

    suspend fun getAllCategories(): List<CategoryDTO> {
        val response = getInstance().getAllCategories()
        val responseBody = response.body()

        return responseBody!!
    }

    suspend fun createCustomizedCategory(params: CreateCustomizedCategoryParamsDTO): CustomizedCategoryDTO {
        val response = getInstance().createCustomizedCategory(params)
        val responseBody = response.body()

        return responseBody!!
    }

    suspend fun updateCustomizedCategory(categoryID: Long, params: GenericValueDTO<Float>): CustomizedCategoryDTO {
        val response = getInstance().updateCustomizedCategory(categoryID, params)
        val responseBody = response.body()

        return responseBody!!
    }

    suspend fun getCustomizedCategoriesByUser(userID: Long) : List<CustomizedCategoryDTO> {
        val response = getInstance().getCustomizedCategoriesByUser(userID)
        val responseBody = response.body()

        return responseBody!!
    }

    suspend fun parseTicket(params: GenericValueDTO<String>): ParsedTicketDTO {
        val response = getInstance().parseTicket(params)
        val responseBody = response.body()

        return responseBody!!
    }

    suspend fun createTicket(params: CreateTicketParamsDTO): TicketDTO {
        val response = getInstance().createTicket(params)
        val responseBody = response.body()

        return responseBody!!
    }

    suspend fun shareTicket(userID: Long, params: ShareTicketParamsDTO): TicketDTO {
        val response = getInstance().shareTicket(userID, params)
        val responseBody = response.body()

        return responseBody!!
    }
}