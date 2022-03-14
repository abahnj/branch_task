package dev.abahnj.branchtask.data.remote

import dev.abahnj.branchtask.data.SessionManager
import okhttp3.Interceptor
import okhttp3.Response
/**
 * Interceptor to add auth token to requests
 */
class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // If token has been saved, add it to the request
        sessionManager.fetchAuthToken()?.let {
            requestBuilder.addHeader("X-Branch-Auth-Token", it)
        }

        return chain.proceed(requestBuilder.build())
    }
}