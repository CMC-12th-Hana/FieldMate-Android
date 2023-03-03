package com.hana.umuljeong.di

import android.content.Context
import com.example.compose_mvvm_sample.data.exception.ResultCallAdapterFactory
import com.hana.umuljeong.App.Companion.ACCESS_TOKEN
import com.hana.umuljeong.BuildConfig
import com.hana.umuljeong.data.AuthAuthenticator
import com.hana.umuljeong.data.AuthInterceptor
import com.hana.umuljeong.data.TokenManager
import com.hana.umuljeong.data.api.AuthService
import com.hana.umuljeong.data.datasource.AuthDataSource
import com.hana.umuljeong.data.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun providesOkHttpClient(
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator
    ) = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        val headerInterceptor = Interceptor {
            val request = it.request()
                .newBuilder()
                .addHeader("Authorization", ACCESS_TOKEN)
                .build()
            return@Interceptor it.proceed(request)
        }
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .authenticator(authAuthenticator)
            .build()
    } else {
        OkHttpClient.Builder().build()
    }

    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("http://fieldmate-env.eba-xwpp7hek.ap-northeast-2.elasticbeanstalk.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .build()

    @Singleton
    @Provides
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor =
        AuthInterceptor(tokenManager)

    @Singleton
    @Provides
    fun provideAuthAuthenticator(tokenManager: TokenManager): AuthAuthenticator =
        AuthAuthenticator(tokenManager)

    @Provides
    @Singleton
    fun providesTokenManager(@ApplicationContext context: Context): TokenManager =
        TokenManager(context)

    @Provides
    @Singleton
    fun providesAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun providesAuthDataSource(
        authService: AuthService,
        tokenManager: TokenManager
    ): AuthDataSource =
        AuthDataSource(authService, tokenManager)

    @Provides
    @Singleton
    fun providesAuthRepository(authDataSource: AuthDataSource): AuthRepository =
        AuthRepository(authDataSource)
}