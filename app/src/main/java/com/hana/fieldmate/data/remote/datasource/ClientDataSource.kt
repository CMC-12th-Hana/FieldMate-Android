package com.hana.fieldmate.data.remote.datasource

import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.api.ClientService
import com.hana.fieldmate.data.remote.model.request.CreateClientReq
import com.hana.fieldmate.data.remote.model.request.UpdateClientReq
import com.hana.fieldmate.data.remote.model.response.ClientListRes
import com.hana.fieldmate.data.remote.model.response.ClientRes
import com.hana.fieldmate.data.remote.model.response.CreateClientRes
import com.hana.fieldmate.data.remote.model.response.UpdateClientRes
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ClientDataSource @Inject constructor(
    private val clientService: ClientService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun createClient(
        companyId: Long,
        createClientReq: CreateClientReq
    ): Flow<ResultWrapper<CreateClientRes>> = flow {
        clientService.createClient(companyId, createClientReq).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.toString()))
        }
    }.flowOn(ioDispatcher)

    fun fetchClientById(clientId: Long): Flow<ResultWrapper<ClientRes>> = flow {
        clientService.fetchClientById(clientId).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.toString()))
        }
    }.flowOn(ioDispatcher)

    fun updateClient(
        clientId: Long,
        updateClientReq: UpdateClientReq
    ): Flow<ResultWrapper<UpdateClientRes>> = flow {
        clientService.updateClient(clientId, updateClientReq).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.toString()))
        }
    }.flowOn(ioDispatcher)

    fun fetchClientList(
        companyId: Long
    ): Flow<ResultWrapper<ClientListRes>> = flow {
        clientService.fetchClientList(companyId).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.toString()))
        }
    }.flowOn(ioDispatcher)
}