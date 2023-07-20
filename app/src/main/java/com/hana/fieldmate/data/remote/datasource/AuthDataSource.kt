package com.hana.fieldmate.data.remote.datasource

import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.api.AuthService
import com.hana.fieldmate.data.remote.model.request.JoinReq
import com.hana.fieldmate.data.remote.model.request.LoginReq
import com.hana.fieldmate.data.remote.model.request.SendMessageReq
import com.hana.fieldmate.data.remote.model.request.VerifyMessageReq
import com.hana.fieldmate.data.remote.model.response.JoinRes
import com.hana.fieldmate.data.remote.model.response.LoginRes
import com.hana.fieldmate.data.remote.model.response.SendMessageRes
import com.hana.fieldmate.data.remote.model.response.VerifyMessageRes
import com.hana.fieldmate.util.TOKEN_EXPIRED_MESSAGE
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    private val authService: AuthService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun login(loginReq: LoginReq): Flow<ResultWrapper<LoginRes>> = flow {
        authService.login(loginReq).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            if (it.message == TOKEN_EXPIRED_MESSAGE) {
                emit(ResultWrapper.Error(ErrorType.JwtExpired(it.message ?: "")))
            } else {
                emit(ResultWrapper.Error(ErrorType.General(it.message ?: "")))
            }
        }
    }.flowOn(ioDispatcher)

    fun join(joinReq: JoinReq): Flow<ResultWrapper<JoinRes>> = flow {
        authService.join(joinReq).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            if (it.message == TOKEN_EXPIRED_MESSAGE) {
                emit(ResultWrapper.Error(ErrorType.JwtExpired(it.message ?: "")))
            } else {
                emit(ResultWrapper.Error(ErrorType.General(it.message ?: "")))
            }
        }
    }.flowOn(ioDispatcher)


    fun verifyMessage(verifyMessageReq: VerifyMessageReq): Flow<ResultWrapper<VerifyMessageRes>> =
        flow {
            authService.verifyMessage(verifyMessageReq).onSuccess {
                emit(ResultWrapper.Success(it))
            }.onFailure {
                if (it.message == TOKEN_EXPIRED_MESSAGE) {
                    emit(ResultWrapper.Error(ErrorType.JwtExpired(it.message ?: "")))
                } else {
                    emit(ResultWrapper.Error(ErrorType.General(it.message ?: "")))
                }
            }
        }.flowOn(ioDispatcher)

    fun sendMessage(sendMessageReq: SendMessageReq): Flow<ResultWrapper<SendMessageRes>> = flow {
        authService.sendMessage(sendMessageReq).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            if (it.message == TOKEN_EXPIRED_MESSAGE) {
                emit(ResultWrapper.Error(ErrorType.JwtExpired(it.message ?: "")))
            } else {
                emit(ResultWrapper.Error(ErrorType.General(it.message ?: "")))
            }
        }
    }.flowOn(ioDispatcher)
}