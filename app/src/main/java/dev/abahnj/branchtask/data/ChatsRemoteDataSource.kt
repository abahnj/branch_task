package dev.abahnj.branchtask.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.abahnj.branchtask.data.model.ChatListResponse
import dev.abahnj.branchtask.data.remote.ApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import  dev.abahnj.branchtask.data.Result.Error
import dev.abahnj.branchtask.data.model.NewChatRequest

class ChatsRemoteDataSource internal constructor(
    private val apiService: ApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ChatDataSource {


    private val observableChats = MutableLiveData<Result<List<ChatListResponse>>>()

    override suspend fun refreshChats() {
        observableChats.value = getChats()!!
    }


    override fun observeChats(): LiveData<Result<List<ChatListResponse>>> {
        return observeChats()
    }

    override suspend fun deleteAllChats() {
    }

    override suspend fun saveChat(chat: NewChatRequest): Result<ChatListResponse> = withContext(ioDispatcher) {
       return@withContext try {
           Result.Success(apiService.saveChat(chat))
       } catch (e: Exception) {
           Error(e)
       }
    }

    override suspend fun saveChat(chat: ChatListResponse) {

    }

    override suspend fun getChats(): Result<List<ChatListResponse>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(apiService.getChats())
        } catch (e: Exception) {
            Error(e)
        }
    }

}
