package com.hana.fieldmate.data.remote.datasource

import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.api.UserInfoService
import com.hana.fieldmate.data.remote.model.response.MemberRes
import com.hana.fieldmate.util.TOKEN_EXPIRED_MESSAGE
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserInfoDataSource @Inject constructor(
    private val userInfoService: UserInfoService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun fetchUserInfo(): Flow<ResultWrapper<MemberRes>> = flow {
        userInfoService.fetchProfile().onSuccess {
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