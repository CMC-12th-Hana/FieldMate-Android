package com.hana.umuljeong.data.remote.datasource

import com.hana.umuljeong.data.ResultWrapper
import com.hana.umuljeong.data.remote.api.AuthService
import com.hana.umuljeong.data.remote.model.request.JoinReq
import com.hana.umuljeong.data.remote.model.request.LoginReq
import com.hana.umuljeong.data.remote.model.response.JoinRes
import com.hana.umuljeong.data.remote.model.response.LoginRes
import com.hana.umuljeong.network.TokenManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    private val authService: AuthService,
    private val tokenManager: TokenManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun login(loginReq: LoginReq): Flow<ResultWrapper<LoginRes>> = flow {
        authService.login(loginReq).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.toString()))
        }
    }.flowOn(ioDispatcher)

    fun join(joinReq: JoinReq): Flow<ResultWrapper<JoinRes>> = flow {
        authService.join(joinReq).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.toString()))
        }
    }.flowOn(ioDispatcher)

    suspend fun saveAccessToken(accessToken: String) =
        tokenManager.saveAccessToken(accessToken)

    suspend fun deleteAccessToken() =
        tokenManager.deleteAccessToken()

    suspend fun setIsLoggedIn(isLoggedIn: Boolean) =
        tokenManager.setIsLoggedIn(isLoggedIn)

    fun getAccessToken(): Flow<String> =
        tokenManager.getAccessToken()

    fun getIsLoggedIn(): Flow<Boolean> =
        tokenManager.getIsLoggedIn()
}