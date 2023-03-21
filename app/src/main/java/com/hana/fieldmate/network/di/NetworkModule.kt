package com.hana.fieldmate.network.di

import android.content.Context
import com.hana.fieldmate.BuildConfig
import com.hana.fieldmate.data.remote.api.*
import com.hana.fieldmate.data.remote.datasource.*
import com.hana.fieldmate.data.remote.repository.*
import com.hana.fieldmate.network.AuthInterceptor
import com.hana.fieldmate.network.EnumConverterFactory
import com.hana.fieldmate.network.exception.ResultCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

enum class NetworkLoadingState {
    LOADING,
    SUCCESS,
    FAILED
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun providesOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AuthInterceptor())
            .build()
    } else {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()
    }

    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(EnumConverterFactory())
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun providesAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun providesClientService(retrofit: Retrofit): ClientService =
        retrofit.create(ClientService::class.java)

    @Provides
    @Singleton
    fun providesCompanyService(retrofit: Retrofit): CompanyService =
        retrofit.create(CompanyService::class.java)

    @Provides
    @Singleton
    fun providesTaskCategoryService(retrofit: Retrofit): TaskCategoryService =
        retrofit.create(TaskCategoryService::class.java)

    @Provides
    @Singleton
    fun providesBusinessService(retrofit: Retrofit): BusinessService =
        retrofit.create(BusinessService::class.java)

    @Provides
    @Singleton
    fun providesTaskService(retrofit: Retrofit): TaskService =
        retrofit.create(TaskService::class.java)

    @Provides
    @Singleton
    fun providesMemberService(retrofit: Retrofit): MemberService =
        retrofit.create(MemberService::class.java)

    @Provides
    @Singleton
    fun providesUserInfoService(retrofit: Retrofit): UserInfoService =
        retrofit.create(UserInfoService::class.java)

    @Provides
    @Singleton
    fun providesAuthDataSource(authService: AuthService): AuthDataSource =
        AuthDataSource(authService)

    @Provides
    @Singleton
    fun providesClientDataSource(clientService: ClientService): ClientDataSource =
        ClientDataSource(clientService)

    @Provides
    @Singleton
    fun providesCompanyDataSource(companyService: CompanyService): CompanyDataSource =
        CompanyDataSource(companyService)

    @Provides
    @Singleton
    fun providesTaskCategoryDataSource(taskCategoryService: TaskCategoryService): TaskCategoryDataSource =
        TaskCategoryDataSource(taskCategoryService)

    @Provides
    @Singleton
    fun providesBusinessDataSource(businessService: BusinessService): BusinessDataSource =
        BusinessDataSource(businessService)

    @Provides
    @Singleton
    fun providesMemberDataSource(memberService: MemberService): MemberDataSource =
        MemberDataSource(memberService)

    @Provides
    @Singleton
    fun providesUserInfoDataSource(userInfoService: UserInfoService): UserInfoDataSource =
        UserInfoDataSource(userInfoService)

    @Provides
    @Singleton
    fun providesTaskDataSource(taskService: TaskService): TaskDataSource =
        TaskDataSource(taskService)

    @Provides
    @Singleton
    fun providesAuthRepository(authDataSource: AuthDataSource): AuthRepository =
        AuthRepository(authDataSource)

    @Provides
    @Singleton
    fun providesClientRepository(clientDataSource: ClientDataSource): ClientRepository =
        ClientRepository(clientDataSource)

    @Provides
    @Singleton
    fun providesCompanyRepository(companyDataSource: CompanyDataSource): CompanyRepository =
        CompanyRepository(companyDataSource)

    @Provides
    @Singleton
    fun providesTaskCategoryRepository(taskCategoryDataSource: TaskCategoryDataSource): TaskCategoryRepository =
        TaskCategoryRepository(taskCategoryDataSource)

    @Provides
    @Singleton
    fun providesBusinessRepository(businessDataSource: BusinessDataSource): BusinessRepository =
        BusinessRepository(businessDataSource)

    @Provides
    @Singleton
    fun providesMemberRepository(memberDataSource: MemberDataSource): MemberRepository =
        MemberRepository(memberDataSource)

    @Provides
    @Singleton
    fun providesUserInfoRepository(userInfoDataSource: UserInfoDataSource): UserInfoRepository =
        UserInfoRepository(userInfoDataSource)

    @Singleton
    @Provides
    fun providesTaskRepository(
        @ApplicationContext context: Context,
        taskDataSource: TaskDataSource
    ): TaskRepository =
        TaskRepository(context, taskDataSource)
}