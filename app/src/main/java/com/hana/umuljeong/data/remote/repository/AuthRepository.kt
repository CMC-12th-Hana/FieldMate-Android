package com.hana.umuljeong.data.remote.repository

import com.hana.umuljeong.data.ResultWrapper
import com.hana.umuljeong.data.remote.datasource.AuthDataSource
import com.hana.umuljeong.data.remote.model.request.JoinReq
import com.hana.umuljeong.data.remote.model.request.LoginReq
import com.hana.umuljeong.data.remote.model.response.JoinRes
import com.hana.umuljeong.data.remote.model.response.LoginRes
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