package dev.abahnj.branchtask.ui.threadList

import androidx.lifecycle.*
import dev.abahnj.branchtask.R
import dev.abahnj.branchtask.data.ChatRepository
import dev.abahnj.branchtask.data.Result
import dev.abahnj.branchtask.data.model.ChatListResponse
import dev.abahnj.branchtask.data.model.NewChatRequest
import dev.abahnj.branchtask.util.Event
import kotlinx.coroutines.launch


class ThreadViewModel(private val chatsRepository: ChatRepository
) : ViewModel() {

    private val _forceUpdate = MutableLiveData(false)
    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _threadId = MutableLiveData(0)
    private val  _threadIdChats : LiveData<List<ChatListResponse>> = _threadId.switchMap {
        MutableLiveData(_sortedThreadsMap.value?.get(it)!!)
    }
    val threadIdChats: LiveData<List<ChatListResponse>> = _threadIdChats


    private val _items: LiveData<List<ChatListResponse>> = _forceUpdate.switchMap { forceUpdate ->
        if (forceUpdate) {
            _dataLoading.value = true
            viewModelScope.launch {
                chatsRepository.refreshChats()
                _dataLoading.value = false
            }
        }
        chatsRepository.observeChats().distinctUntilChanged().switchMap { filterChats(it) }
    }

    val items: LiveData<List<ChatListResponse>> = _items

    private val _sortedThreadsMap = MutableLiveData<Map<Int, List<ChatListResponse>>>()
    val sortedThreadsMap : LiveData<Map<Int, List<ChatListResponse>>> = _sortedThreadsMap

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    // Not used at the moment
    private val isDataLoadingError = MutableLiveData<Boolean>()

    // This LiveData depends on another so we can use a transformation.
    val empty: LiveData<Boolean> = Transformations.map(_items) {
        it.isEmpty()
    }

    init {
        // Set initial state
        loadChats(true)
    }
    /**
     * @param forceUpdate Pass in true to refresh the data in the [TasksDataSource]
     */
    fun loadChats(forceUpdate: Boolean) {
        _forceUpdate.value = forceUpdate
    }

    fun getChatsByThreadId(threadId: Int) {
        _threadId.value = threadId
    }

    fun createChat(newChat: NewChatRequest) = viewModelScope.launch {
        chatsRepository.saveChat(newChat)
        //_taskUpdatedEvent.value = Event(Unit)
    }



    private fun filterChats(chatsResult: Result<List<ChatListResponse>>): LiveData<List<ChatListResponse>> {
        // TODO: This is a good case for liveData builder. Replace when stable.
        val result = MutableLiveData<List<ChatListResponse>>()

        if (chatsResult is Result.Success) {
            isDataLoadingError.value = false
            viewModelScope.launch {
                result.value = filterItems(chatsResult.data)
            }
        } else {
            result.value = emptyList()
            showSnackbarMessage(R.string.login_failed)
            isDataLoadingError.value = true
        }

        return result
    }

    private fun filterItems(chats: List<ChatListResponse>): List<ChatListResponse> {
        val threads = chats.groupBy { it.threadId }
        val sortedThread = threads.entries.sortedBy { s -> s.value.maxByOrNull { t -> t.timestamp }!!.timestamp }

        _sortedThreadsMap.value = sortedThread.associate { it.key to it.value }

        return sortedThread.map { it.value.first() }
    }

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }

}