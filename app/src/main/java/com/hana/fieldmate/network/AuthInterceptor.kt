package com.hana.fieldmate.network

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val authManager: AuthManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking { authManager.getAccessToken().first() }
        val request = chain.request().newBuilder()
        request.addHeader("Authorization", "Bearer $accessToken")

        return chain.proceed(request.build())
    }
}