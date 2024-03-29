package com.hana.fieldmate.domain.di

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
    fun providesFetchBusinessListByClientIdUseCase(businessRepository: BusinessRepository): FetchBusinessListByClientIdUseCase =
        FetchBusinessListByClientIdUseCase(businessRepository)

    @Singleton
    @Provides
    fun providesFetchBusinessList(businessRepository: BusinessRepository): FetchBusinessListUseCase =
        FetchBusinessListUseCase(businessRepository)

    @Singleton
    @Provides
    fun providesDeleteBusinessUseCase(businessRepository: BusinessRepository): DeleteBusinessUseCase =
        DeleteBusinessUseCase(businessRepository)

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
    fun providesUpdateTaskUseCase(taskRepository: TaskRepository): UpdateTaskUseCase =
        UpdateTaskUseCase(taskRepository)

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
    fun providesFetchTaskGraphByClientIdUseCase(taskRepository: TaskRepository): FetchTaskGraphByClientIdUseCase =
        FetchTaskGraphByClientIdUseCase(taskRepository)

    @Singleton
    @Provides
    fun providesFetchTaskGraphByBusinessIdUseCase(taskRepository: TaskRepository): FetchTaskGraphByBusinessIdUseCase =
        FetchTaskGraphByBusinessIdUseCase(taskRepository)

    @Singleton
    @Provides
    fun providesFetchTaskListByDateUseCase(taskRepository: TaskRepository): FetchTaskListByDateUseCase =
        FetchTaskListByDateUseCase(taskRepository)

    @Singleton
    @Provides
    fun providesFetchTaskListUseCase(taskRepository: TaskRepository): FetchTaskListUseCase =
        FetchTaskListUseCase(taskRepository)

    // MemberUseCase
    @Singleton
    @Provides
    fun providesCreateMemberUseCase(memberRepository: MemberRepository): CreateMemberUseCase =
        CreateMemberUseCase(memberRepository)

    @Singleton
    @Provides
    fun providesUpdateMemberToLeaderUseCase(memberRepository: MemberRepository): UpdateMemberToLeaderUseCase =
        UpdateMemberToLeaderUseCase(memberRepository)

    @Singleton
    @Provides
    fun providesFetchProfileByIdUseCase(memberRepository: MemberRepository): FetchProfileByIdUseCase =
        FetchProfileByIdUseCase(memberRepository)

    @Singleton
    @Provides
    fun providesUpdateMemberProfileUseCase(memberRepository: MemberRepository): UpdateMemberProfileUseCase =
        UpdateMemberProfileUseCase(memberRepository)

    @Singleton
    @Provides
    fun providesUpdateMyProfileUseCase(memberRepository: MemberRepository): UpdateMyProfileUseCase =
        UpdateMyProfileUseCase(memberRepository)

    @Singleton
    @Provides
    fun providesUpdateMyPassword(memberRepository: MemberRepository): UpdateMyPasswordUseCase =
        UpdateMyPasswordUseCase(memberRepository)

    @Singleton
    @Provides
    fun providesFetchMemberListUseCase(memberRepository: MemberRepository): FetchMemberListUseCase =
        FetchMemberListUseCase(memberRepository)

    @Singleton
    @Provides
    fun providesQuitMemberUseCase(memberRepository: MemberRepository): QuitMemberUseCase =
        QuitMemberUseCase(memberRepository)

    @Singleton
    @Provides
    fun providesDeleteMemberUseCase(memberRepository: MemberRepository): DeleteMemberUseCase =
        DeleteMemberUseCase(memberRepository)

    // UserInfoUseCase
    @Singleton
    @Provides
    fun providesFetchUserInfoUseCase(userInfoRepository: UserInfoRepository): FetchUserInfoUseCase =
        FetchUserInfoUseCase(userInfoRepository)

}