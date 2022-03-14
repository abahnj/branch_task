package dev.abahnj.branchtask.data.model

import com.google.gson.annotations.SerializedName

data class ChatListResponse(
    val id: Int,
    @SerializedName("thread_id")
    val threadId: Int,
    @SerializedName("user_id")
    val userId: String,
    val body: String,
    val timestamp: String,
    @SerializedName("agent_id")
    val agent_id: String?
)
