package com.hana.umuljeong.data

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val tokenManager: TokenManager
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val token = runBlocking { tokenManager.getAccessToken() }

        return runBlocking {
            val newToken = tokenManager.getAccessToken()

            response.request.newBuilder()
                .header("Authorization", "").build()

            /* 후에 토큰 refresh 구현하면 적용
            if (!newToken.isSuccessful || newToken.body() == null) {
                tokenManager.deleteAccessToken()
            }

            newToken.body()?.let {
                tokenManager.saveAccessToken(it.token)
                response.request.newBuilder()
                    .header("Authorization", "${it.token}")
                    .build()
            }
             */
        }
    }

    /* 후에 토큰 refresh 구현하면 적용
    private suspend fun updateToken(refreshToken: String?): retrofit2.Response<LoginRes> {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://jwt-test-api.onrender.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        val service = retrofit.create(AuthService::class.java)

        return service.refreshToken("$refreshToken")
    }
     */
}