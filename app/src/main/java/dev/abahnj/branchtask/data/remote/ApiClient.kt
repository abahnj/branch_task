package dev.abahnj.branchtask.data.remote

import dev.abahnj.branchtask.data.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Constants {

    // Endpoints
    const val BASE_URL = "https://android-messaging.branch.co/api/"
    const val LOGIN_URL = "login"
    const val CHATS_URL = "messages"


}
class ApiClient(private val sessionManager: SessionManager) {

    private lateinit var apiService: ApiService

    fun getApiService(): ApiService {

        // Initialize ApiService if not initialized yet
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClient())

                .build()

            apiService = retrofit.create(ApiService::class.java)
        }

        return apiService
    }

    private val logInterceptor =  HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }


    /**
     * Initialize OkhttpClient with our interceptor
     */
    private fun okhttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionManager))
            //.addInterceptor(logInterceptor)
            .build()
    }

}
