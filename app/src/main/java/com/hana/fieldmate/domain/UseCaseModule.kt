package com.hana.fieldmate.domain

import com.hana.fieldmate.data.remote.repository.*
import com.hana.fieldmate.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    // AuthUseCase
    @Singleton
    @Provides
    fun providesLoginUseCase(authRepository: AuthRepository): LoginUseCase =
        LoginUseCase(authRepository)

    @Singleton
    @Provides
    fun providesJoinUseCase(authRepository: AuthRepository): JoinUseCase =
        JoinUseCase(authRepository)

    @Singleton
    @Provides
    fun providesVerifyMessageUseCase(authRepository: AuthRepository): VerifyMessageUseCase =
        VerifyMessageUseCase(authRepository)

    @Singleton
    @Provides
    fun providesSendMessageUseCase(authRepository: AuthRepository): SendMessageUseCase =
        SendMessageUseCase(authRepository)

    // BusinessUseCase
    @Singleton
    @Provides
    fun providesCreateBusinessUseCase(businessRepository: BusinessRepository): CreateBusinessUseCase =
        CreateBusinessUseCase(businessRepository)

    @Singleton
    @Provides
    fun providesFetchBusinessByIdUseCase(businessRepository: BusinessRepository): FetchBusinessByIdUseCase =
        FetchBusinessByIdUseCase(businessRepository)

    @Singleton
    @Provides
    fun providesUpdateBusinessUseCase(businessRepository: BusinessRepository): UpdateBusinessUseCase =
        UpdateBusinessUseCase(businessRepository)

    // ClientUseCase
    @Singleton
    @Provides
    fun providesCreateClientUseCase(clientRepository: ClientRepository): CreateClientUseCase =
        CreateClientUseCase(clientRepository)

    @Singleton
    @Provides
    fun providesFetchClientByIdUseCase(clientRepository: ClientRepository): FetchClientByIdUseCase =
        FetchClientByIdUseCase(clientRepository)

    @Singleton
    @Provides
    fun providesUpdateClientUseCase(clientRepository: ClientRepository): UpdateClientUseCase =
        UpdateClientUseCase(clientRepository)

    @Singleton
    @Provides
    fun providesDeleteClientUseCase(clientRepository: ClientRepository): DeleteClientUseCase =
        DeleteClientUseCase(clientRepository)

    @Singleton
    @Provides
    fun providesFetchClientListUseCase(clientRepository: ClientRepository): FetchClientListUseCase =
        FetchClientListUseCase(clientRepository)

    // CompanyUseCase
    @Singleton
    @Provides
    fun providesCreateCompanyUseCase(companyRepository: CompanyRepository): CreateCompanyUseCase =
        CreateCompanyUseCase(companyRepository)

    @Singleton
    @Provides
    fun providesJoinCompanyUseCase(companyRepository: CompanyRepository): JoinCompanyUseCase =
        JoinCompanyUseCase(companyRepository)

    // TaskCategoryUseCase
    @Singleton
    @Provides
    fun providesCreateTaskCategoryUseCase(taskCategoryRepository: TaskCategoryRepository): CreateTaskCategoryUseCase =
        CreateTaskCategoryUseCase(taskCategoryRepository)

    @Singleton
    @Provides
    fun providesUpdateTaskCategoryUseCase(taskCategoryRepository: TaskCategoryRepository): UpdateTaskCategoryUseCase =
        UpdateTaskCategoryUseCase(taskCategoryRepository)

    @Singleton
    @Provides
    fun providesFetchTaskCategoryListUseCase(taskCategoryRepository: TaskCategoryRepository): FetchTaskCategoryListUseCase =
        FetchTaskCategoryListUseCase(taskCategoryRepository)

    @Singleton
    @Provides
    fun providesDeleteTaskCategoryUseCase(taskCategoryRepository: TaskCategoryRepository): DeleteTaskCategoryUseCase =
        DeleteTaskCategoryUseCase(taskCategoryRepository)

    // TaskUseCase
    @Singleton
    @Provides
    fun providesCreateTaskUseCase(taskRepository: TaskRepository): CreateTaskUseCase =
        CreateTaskUseCase(taskRepository)

    @Singleton
    @Provides
    fun providesDeleteTaskUseCase(taskRepository: TaskRepository): DeleteTaskUseCase =
        DeleteTaskUseCase(taskRepository)

    @Singleton
    @Provides
    fun providesFetchTaskByIdUseCase(taskRepository: TaskRepository): FetchTaskByIdUseCase =
        FetchTaskByIdUseCase(taskRepository)

    @Singleton
    @Provides
    fun providesFetchTaskListUseCase(taskRepository: TaskRepository): FetchTaskListUseCase =
        FetchTaskListUseCase(taskRepository)

    @Singleton
    @Provides
    fun providesCreateMemberUseCase(memberRepository: MemberRepository): CreateMemberUseCase =
        CreateMemberUseCase(memberRepository)

    @Singleton
    @Provides
    fun providesFetchProfileByIdUseCase(memberRepository: MemberRepository): FetchProfileByIdUseCase =
        FetchProfileByIdUseCase(memberRepository)

    @Singleton
    @Provides
    fun providesUpdateProfileUseCase(memberRepository: MemberRepository): UpdateProfileUseCase =
        UpdateProfileUseCase(memberRepository)

    @Singleton
    @Provides
    fun providesFetchMemberListUseCase(memberRepository: MemberRepository): FetchMemberListUseCase =
        FetchMemberListUseCase(memberRepository)

    // UserInfoUseCase
    @Singleton
    @Provides
    fun providesFetchUserInfoUseCase(userInfoRepository: UserInfoRepository): FetchUserInfoUseCase =
        FetchUserInfoUseCase(userInfoRepository)

}