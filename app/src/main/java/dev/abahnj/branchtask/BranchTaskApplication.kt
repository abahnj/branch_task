package dev.abahnj.branchtask

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import dev.abahnj.branchtask.data.ChatRepository
import dev.abahnj.branchtask.data.LoginRepository
import dev.abahnj.branchtask.data.SessionManager
import dev.abahnj.branchtask.util.ServiceLocator
import timber.log.Timber

private const val USER_PREFERENCES_NAME = "user_preferences"

private val Context.dataStore by preferencesDataStore(
    name = USER_PREFERENCES_NAME,
)


class BranchTaskApplication : Application() {

    val chatRepository: ChatRepository
        get() = ServiceLocator.provideChatRepository(this)

    val loginRepository: LoginRepository
        get() = ServiceLocator.provideLoginRepository(this)

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}