package com.hana.umuljeong.data.repository

import com.hana.umuljeong.data.Result
import com.hana.umuljeong.data.datasource.AuthDataSource
import com.hana.umuljeong.data.model.request.JoinReq
import com.hana.umuljeong.data.model.request.LoginReq
import com.hana.umuljeong.data.model.response.JoinRes
import com.hana.umuljeong.data.model.response.LoginRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authDataSource: AuthDataSource
) {
    fun login(phoneNumber: String, password: String): Flow<Result<LoginRes>> =
        authDataSource.login(LoginReq(phoneNumber, password))

    fun join(
        name: String,
        phoneNumber: String,
        password: String,
        passwordCheck: String
    ): Flow<Result<JoinRes>> =
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