package dev.abahnj.branchtask.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.abahnj.branchtask.data.model.ChatListResponse
import dev.abahnj.branchtask.data.model.NewChatRequest

class ChatLocalDataSource: ChatDataSource {
    private var CHAT_DATA = LinkedHashMap<String, ChatListResponse>(2)
    private val observableChats = MutableLiveData<Result<List<ChatListResponse>>>()

    init {
        addChat(1, 2, "hdfhgj", "kjgmhgjhgjkjhkhjhvhjggjhgjkjhhk", "hjgjh")
        addChat(3, 7, "hdfgj", "kjgmhgjhgjghgjkjhkhjhvhjggjhgjkjhhk", "hjjh")
    }

    override fun observeChats(): LiveData<Result<List<ChatListResponse>>> = observableChats

    override suspend fun getChats(): Result<List<ChatListResponse>> {
        val tasks = CHAT_DATA.values.toList()
        return Result.Success(tasks)
    }

    override suspend fun refreshChats() {
        observableChats.value = getChats()!!
    }

    override suspend fun deleteAllChats() {
        CHAT_DATA.clear()
    }

    override suspend fun saveChat(chat: NewChatRequest): Result<ChatListResponse>{
       // CHAT_DATA[chat.id.toString()] = chat
      //  observableChats.value = Result.Success(CHAT_DATA.values.toList())
        return Result.Error(Exception())
    }

    override suspend fun saveChat(chat: ChatListResponse) {
        CHAT_DATA[chat.id.toString()] = chat
        observableChats.value = Result.Success(CHAT_DATA.values.toList())
    }

    private fun addChat(id: Int, threadId: Int, userId: String, body: String, timestamp: String) {
        val newTask = ChatListResponse(id, threadId, userId, body, timestamp, null)
        CHAT_DATA[newTask.id.toString()] = newTask
        observableChats.value = Result.Success(CHAT_DATA.values.toList())
    }
}
