package com.hana.fieldmate.network

import android.util.Log
import com.hana.fieldmate.App
import com.hana.fieldmate.BuildConfig
import com.hana.fieldmate.data.remote.api.AuthService
import com.hana.fieldmate.data.remote.model.request.UpdateTokenReq
import com.hana.fieldmate.data.remote.model.response.UpdateTokenRes
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthAuthenticator : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val token = runBlocking { App.getInstance().getDataStore().getRefreshToken().first() }

        Log.d("리프레쉬 토큰", token)

        return runBlocking {
            val newToken = updateToken(token)

            Log.d("액세스 토큰 재발급", newToken.body()?.accessToken ?: "")

            if (!newToken.isSuccessful || newToken.body() == null) {
                App.getInstance().getDataStore().deleteAccessToken()
            }

            newToken.body()?.let {
                App.getInstance().getDataStore().saveAccessToken(it.accessToken)
                response.request.newBuilder()
                    .header("Authorization", it.accessToken)
                    .build()
            }
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
}