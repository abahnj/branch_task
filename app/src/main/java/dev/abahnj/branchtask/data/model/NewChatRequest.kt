package dev.abahnj.branchtask.data.model

import com.google.gson.annotations.SerializedName

data class NewChatRequest(
    @SerializedName("thread_id")
    val threadId: Int,
    val body: String,
)
