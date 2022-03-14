
import androidx.lifecycle.LiveData
import com.example.android.architecture.blueprints.todoapp.util.wrapEspressoIdlingResource
import dev.abahnj.branchtask.data.ChatDataSource
import dev.abahnj.branchtask.data.ChatRepository
import dev.abahnj.branchtask.data.model.ChatListResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import dev.abahnj.branchtask.data.Result
import dev.abahnj.branchtask.data.model.NewChatRequest
import dev.abahnj.branchtask.data.succeeded

/**
 * Default implementation of [ChatRepository]. Single entry point for managing chats' data.
 */
class DefaultChatRepository(
    private val chatRemoteDataSource: ChatDataSource,
    private val chatLocalDataSource: ChatDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ChatRepository {

    override suspend fun getChats(forceUpdate: Boolean): Result<List<ChatListResponse>> {
        // Set app as busy while this function executes.
        wrapEspressoIdlingResource {

            if (forceUpdate) {
                try {
                    updateChatsFromRemoteDataSource()
                } catch (ex: Exception) {
                    return Result.Error(ex)
                }
            }
            return chatLocalDataSource.getChats()
        }
    }

    override suspend fun refreshChats() {
        updateChatsFromRemoteDataSource()
    }

    override suspend fun saveChat(chat: ChatListResponse) {
        TODO("Not yet implemented")
    }


    override fun observeChats(): LiveData<Result<List<ChatListResponse>>> {
        return chatLocalDataSource.observeChats()
    }

//    override suspend fun refreshTask(taskId: String) {
//        updateChatsFromRemoteDataSource(taskId)
//    }

    private suspend fun updateChatsFromRemoteDataSource() {
        val remoteChats = chatRemoteDataSource.getChats()

        if (remoteChats is Result.Success) {
            // Real apps might want to do a proper sync, deleting, modifying or adding each task.
            chatLocalDataSource.deleteAllChats()
            remoteChats.data.forEach {
                chatLocalDataSource.saveChat(it)
            }
        } else if (remoteChats is Result.Error) {
            throw remoteChats.exception
        }
    }

    override suspend fun saveChat(chat: NewChatRequest) {
        coroutineScope {
            launch {
                val response = chatRemoteDataSource.saveChat(chat)
                if (response.succeeded){
                    chatLocalDataSource.saveChat((response as Result.Success).data)
                }

            }
            launch { chatLocalDataSource.saveChat(chat) }
        }
    }

}
