package com.hana.fieldmate.network

import com.hana.fieldmate.App
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking { App.getInstance().getDataStore().getAccessToken().first() }
        val request = chain.request().newBuilder()
        request.addHeader("Authorization", "Bearer $accessToken")

        return chain.proceed(request.build())
    }
}