package com.hana.fieldmate.data.remote.datasource

import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.api.BusinessService
import com.hana.fieldmate.data.remote.model.request.CreateBusinessReq
import com.hana.fieldmate.data.remote.model.request.UpdateBusinessReq
import com.hana.fieldmate.data.remote.model.response.BusinessRes
import com.hana.fieldmate.data.remote.model.response.CreateBusinessRes
import com.hana.fieldmate.data.remote.model.response.UpdateBusinessRes
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class BusinessDataSource @Inject constructor(
    private val businessService: BusinessService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun createBusiness(
        clientId: Long,
        createBusinessReq: CreateBusinessReq
    ): Flow<ResultWrapper<CreateBusinessRes>> = flow {
        businessService.createBusiness(clientId, createBusinessReq).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.message!!))
        }
    }.flowOn(ioDispatcher)

    fun fetchBusinessById(
        businessId: Long
    ): Flow<ResultWrapper<BusinessRes>> = flow {
        businessService.fetchBusinessById(businessId).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.message!!))
        }
    }.flowOn(ioDispatcher)

    fun updateBusiness(
        businessId: Long,
        updateBusinessReq: UpdateBusinessReq
    ): Flow<ResultWrapper<UpdateBusinessRes>> = flow {
        businessService.updateBusiness(businessId, updateBusinessReq).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.message!!))
        }
    }.flowOn(ioDispatcher)
}