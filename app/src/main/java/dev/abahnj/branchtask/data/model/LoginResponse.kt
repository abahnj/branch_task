package dev.abahnj.branchtask.data.model

import com.google.gson.annotations.SerializedName


data class LoginRequest(val username: String, val password: String)

data class LoginResponse(
    @SerializedName("auth_token")
    val authToken: String)
