package com.hana.fieldmate.network

import com.hana.fieldmate.App
import com.hana.fieldmate.BuildConfig
import com.hana.fieldmate.data.remote.api.AuthService
import com.hana.fieldmate.data.remote.model.request.UpdateTokenReq
import com.hana.fieldmate.data.remote.model.response.UpdateTokenRes
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking { App.getInstance().getDataStore().getAccessToken().first() }
        val originalRequest = chain.request()
        val authenticationRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken").build()
        val response = chain.proceed(authenticationRequest)

        when (response.code) {
            403, 401 -> {
                val newToken = runBlocking {
                    updateToken(App.getInstance().getDataStore().getRefreshToken().first())
                }

                if (newToken.body()?.accessToken == "" || newToken.code() != 200) {
                    runBlocking {
                        App.getInstance().getDataStore().deleteAccessToken()
                        App.getInstance().getDataStore().deleteRefreshToken()
                    }
                } else {
                    val newAuthenticationResponse =
                        chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer ${newToken.body()?.accessToken}")
                            .build()

                    response.close()

                    return chain.proceed(newAuthenticationResponse)
                }
            }
        }

        return response
    }
}

private suspend fun updateToken(refreshToken: String?): retrofit2.Response<UpdateTokenRes> {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
    val service = retrofit.create(AuthService::class.java)

    return service.updateToken(UpdateTokenReq(refreshToken ?: ""))
}