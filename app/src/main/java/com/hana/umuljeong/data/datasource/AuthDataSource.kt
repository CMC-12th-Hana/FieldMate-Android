package com.hana.umuljeong.data.datasource

import com.hana.umuljeong.data.Result
import com.hana.umuljeong.data.TokenManager
import com.hana.umuljeong.data.api.AuthService
import com.hana.umuljeong.data.model.request.JoinReq
import com.hana.umuljeong.data.model.request.LoginReq
import com.hana.umuljeong.data.model.response.JoinRes
import com.hana.umuljeong.data.model.response.LoginRes
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
    fun login(loginReq: LoginReq): Flow<Result<LoginRes>> = flow {
        val result = authService.login(loginReq)

        if (result.isSuccessful) {
            result.body()?.let {
                emit(Result.Success(it))
            }
        } else {
            result.errorBody()?.let {
                emit(Result.Error(it.toString()))
            }
        }
    }.flowOn(ioDispatcher)

    fun join(joinReq: JoinReq): Flow<Result<JoinRes>> = flow {
        val result = authService.join(joinReq)

        if (result.isSuccessful) {
            result.body()?.let {
                emit(Result.Success(it))
            }
        } else {
            result.errorBody()?.let {
                emit(Result.Error(it.toString()))
            }
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