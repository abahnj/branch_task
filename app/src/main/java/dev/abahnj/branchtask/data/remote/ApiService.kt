package dev.abahnj.branchtask.data.remote

import dev.abahnj.branchtask.data.model.ChatListResponse
import dev.abahnj.branchtask.data.model.LoginRequest
import dev.abahnj.branchtask.data.model.LoginResponse
import dev.abahnj.branchtask.data.model.NewChatRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST(Constants.LOGIN_URL)
    suspend fun login(@Body request: LoginRequest) : LoginResponse

    @GET(Constants.CHATS_URL)
    suspend fun getChats(): List<ChatListResponse>

    @POST(Constants.CHATS_URL)
    suspend fun saveChat(@Body request: NewChatRequest) : ChatListResponse
}

