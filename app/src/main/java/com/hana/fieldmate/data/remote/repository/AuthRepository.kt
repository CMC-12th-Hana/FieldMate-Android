package com.hana.fieldmate.data.remote.repository

import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.datasource.AuthDataSource
import com.hana.fieldmate.data.remote.model.request.JoinReq
import com.hana.fieldmate.data.remote.model.request.LoginReq
import com.hana.fieldmate.data.remote.model.request.SendMessageReq
import com.hana.fieldmate.data.remote.model.request.VerifyMessageReq
import com.hana.fieldmate.data.remote.model.response.JoinRes
import com.hana.fieldmate.data.remote.model.response.LoginRes
import com.hana.fieldmate.data.remote.model.response.SendMessageRes
import com.hana.fieldmate.data.remote.model.response.VerifyMessageRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authDataSource: AuthDataSource
) {
    fun login(phoneNumber: String, password: String): Flow<ResultWrapper<LoginRes>> =
        authDataSource.login(LoginReq(phoneNumber, password))

    fun join(
        name: String,
        phoneNumber: String,
        password: String,
        passwordCheck: String
    ): Flow<ResultWrapper<JoinRes>> =
        authDataSource.join(JoinReq(name, phoneNumber, password, passwordCheck))

    fun verifyMessage(
        phoneNumber: String,
        authenticationNumber: String
    ): Flow<ResultWrapper<VerifyMessageRes>> =
        authDataSource.verifyMessage(VerifyMessageReq(phoneNumber, authenticationNumber, "JOIN"))

    fun sendMessage(phoneNumber: String): Flow<ResultWrapper<SendMessageRes>> =
        authDataSource.sendMessage(SendMessageReq(phoneNumber, "JOIN"))

    suspend fun saveAccessToken(accessToken: String) =
        authDataSource.saveAccessToken(accessToken)

    suspend fun deleteAccessToken() =
        authDataSource.deleteAccessToken()

    suspend fun setIsLoggedIn(isLoggedIn: Boolean) =
        authDataSource.setIsLoggedIn(isLoggedIn)

    fun getAccessToken(): Flow<String> =
        authDataSource.getAccessToken()

    fun getIsLoggedIn(): Flow<Boolean> =
        authDataSource.getIsLoggedIn()
}