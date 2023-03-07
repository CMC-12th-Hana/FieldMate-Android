package com.hana.fieldmate.data.remote.datasource

import android.util.Log
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.api.AuthService
import com.hana.fieldmate.data.remote.model.request.JoinReq
import com.hana.fieldmate.data.remote.model.request.LoginReq
import com.hana.fieldmate.data.remote.model.response.JoinRes
import com.hana.fieldmate.data.remote.model.response.LoginRes
import com.hana.fieldmate.network.TokenManager
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
            Log.d("회원 가입", "성공")
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