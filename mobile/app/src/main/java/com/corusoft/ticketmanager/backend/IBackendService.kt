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
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface IBackendService {
    /* ******************** USER ENDPOINTS ******************** */
    @POST("users/register")
    suspend fun register(@Body params: RegisterUserParamsDTO): Call<AuthenticatedUserDTO>

    @POST("users/login")
    suspend fun login(@Body params: LoginParamsDTO): Response<AuthenticatedUserDTO>

    @POST("users/login/token")
    suspend fun loginFromToken(): Response<AuthenticatedUserDTO>

    @POST("users/subscribe/{userID}")
    suspend fun subscribeToPremium(@Path("userID") id: Long): Response<SubscriptionDTO>


    /* ******************** TICKET ENDPOINTS ******************** */
    @GET("tickets/categories")
    suspend fun getAllCategories(): Response<List<CategoryDTO>>

    @POST("tickets/categories")
    suspend fun createCustomizedCategory(@Body params: CreateCustomizedCategoryParamsDTO): Response<CustomizedCategoryDTO>

    @PUT("tickets/categories/{categoryID}")
    suspend fun updateCustomizedCategory(@Path("categoryID") id: Long, @Body params: GenericValueDTO<Float>): Response<CustomizedCategoryDTO>

    @GET("tickets/categories/{userID}")
    suspend fun getCustomizedCategoriesByUser(@Path("userID") id: Long) : Response<List<CustomizedCategoryDTO>>

    @POST("tickets/parse")
    suspend fun parseTicket(@Body params: GenericValueDTO<String>): Response<ParsedTicketDTO>

    @POST("tickets/")
    suspend fun createTicket(@Body params: CreateTicketParamsDTO): Response<TicketDTO>

    @POST("share/{ticketID}")
    suspend fun shareTicket(@Path("userID") id: Long, @Body params: ShareTicketParamsDTO): Response<TicketDTO>

    /* ******************** STATS ENDPOINTS ******************** */


}