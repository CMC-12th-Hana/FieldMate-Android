package com.hana.fieldmate.data.remote.datasource

import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.api.CompanyService
import com.hana.fieldmate.data.remote.model.request.CreateCompanyReq
import com.hana.fieldmate.data.remote.model.response.CreateCompanyRes
import com.hana.fieldmate.data.remote.model.response.JoinCompanyRes
import com.hana.fieldmate.util.TOKEN_EXPIRED_MESSAGE
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CompanyDataSource @Inject constructor(
    private val companyService: CompanyService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun createCompany(
        createCompanyReq: CreateCompanyReq
    ): Flow<ResultWrapper<CreateCompanyRes>> = flow {
        companyService.createCompany(createCompanyReq).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            if (it.message == TOKEN_EXPIRED_MESSAGE) {
                emit(ResultWrapper.Error(ErrorType.JwtExpired(it.message ?: "")))
            } else {
                emit(ResultWrapper.Error(ErrorType.General(it.message ?: "")))
            }
        }
    }.flowOn(ioDispatcher)

    fun joinCompany(): Flow<ResultWrapper<JoinCompanyRes>> = flow {
        companyService.joinCompany().onSuccess {
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