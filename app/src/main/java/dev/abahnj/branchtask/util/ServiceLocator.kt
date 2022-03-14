/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.abahnj.branchtask.util

import DefaultChatRepository
import android.content.Context
import androidx.annotation.VisibleForTesting
import dev.abahnj.branchtask.data.*
import dev.abahnj.branchtask.data.remote.ApiClient
import dev.abahnj.branchtask.data.remote.ApiService


/**
 * A Service Locator for the [ChatRepository]. This is the prod version, with a
 * the "real" [ChatsRemoteDataSource].
 */
object ServiceLocator {

    private val lock = Any()
    @Volatile
    var chatsRepository: ChatRepository? = null
        @VisibleForTesting set
    @Volatile
    var loginRepository: LoginRepository? = null
        @VisibleForTesting set

    private var apiService: ApiService? = null
    private var sessionManager: SessionManager? = null
    private var chatLocalDataSource: ChatLocalDataSource? = null

    fun provideChatRepository(context: Context): ChatRepository {
        synchronized(this) {
            return chatsRepository ?: chatsRepository ?: createChatsRepository(context)
        }
    }

    fun provideLoginRepository(context: Context): LoginRepository {
        synchronized(this) {
            return loginRepository ?: loginRepository ?: createLoginRepository(context)
        }
    }

    private fun createLoginRepository(context: Context): LoginRepository {
        val apiService = apiService ?: createApiClient(context)
        val sessionManager = sessionManager ?: createSessionManager(context)

        val newRepo =
            LoginRepository(LoginDataSource(apiService, sessionManager))
        loginRepository = newRepo
        return newRepo
    }

    private fun createChatsRepository(context: Context): ChatRepository {
        val apiService = apiService ?: createApiClient(context)

        val newRepo =
            DefaultChatRepository(ChatsRemoteDataSource(apiService), createLocalDataSource())
        chatsRepository = newRepo
        return newRepo
    }

    private fun createApiClient(context: Context) : ApiService {
        val sessionManager = sessionManager ?: createSessionManager(context)
        return ApiClient(sessionManager).getApiService()
    }

    private fun createLocalDataSource(): ChatLocalDataSource =
        this.chatLocalDataSource ?: ChatLocalDataSource()

    private fun createSessionManager(context: Context): SessionManager = SessionManager(context)


}

