package dev.abahnj.branchtask.data

import androidx.lifecycle.LiveData
import dev.abahnj.branchtask.data.model.ChatListResponse
import dev.abahnj.branchtask.data.Result
import dev.abahnj.branchtask.data.model.NewChatRequest

/**
 * Interface to the data layer.
 */
interface ChatRepository {

    fun observeChats(): LiveData<Result<List<ChatListResponse>>>

    suspend fun getChats(forceUpdate: Boolean = false): Result<List<ChatListResponse>>

    suspend fun refreshChats()

    suspend fun saveChat(chat: ChatListResponse)

    suspend fun saveChat(chat: NewChatRequest)

}
