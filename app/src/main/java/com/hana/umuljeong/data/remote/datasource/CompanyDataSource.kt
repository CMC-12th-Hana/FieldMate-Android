package com.hana.umuljeong.data.remote.datasource

import com.hana.umuljeong.data.ResultWrapper
import com.hana.umuljeong.data.remote.api.CompanyService
import com.hana.umuljeong.data.remote.model.request.CreateCompanyReq
import com.hana.umuljeong.data.remote.model.response.CreateCompanyRes
import com.hana.umuljeong.data.remote.model.response.JoinCompanyRes
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
            emit(ResultWrapper.Error(it.toString()))
        }
    }.flowOn(ioDispatcher)

    fun joinCompany(): Flow<ResultWrapper<JoinCompanyRes>> = flow<ResultWrapper<JoinCompanyRes>> {
        companyService.joinCompany().onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.toString()))
        }
    }.flowOn(ioDispatcher)
}