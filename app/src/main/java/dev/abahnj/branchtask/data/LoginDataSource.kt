package dev.abahnj.branchtask.data

import android.content.Context
import androidx.datastore.core.DataStore
import dev.abahnj.branchtask.data.model.LoggedInUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import dev.abahnj.branchtask.data.Result.Success
import dev.abahnj.branchtask.data.Result.Error
import dev.abahnj.branchtask.data.model.LoginRequest
import dev.abahnj.branchtask.data.model.LoginResponse
import dev.abahnj.branchtask.data.remote.ApiClient
import dev.abahnj.branchtask.data.remote.ApiService
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource internal constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

   suspend fun login(username: String, password: String): Result<LoginResponse> =  withContext(Dispatchers.IO) {
       return@withContext try {
           val response = apiService.login(LoginRequest(username, password))
           sessionManager.saveAuthToken(response.authToken)
           Timber.d(response.toString())
           Success(response)
       } catch (e: Exception) {
           Error(e)
       }
    }

    /*{

        try {
            // TODO: handle loggedInUser authentication
            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }*/

    fun logout() {
        // TODO: revoke authentication
    }
}