package com.hana.fieldmate.data.remote.datasource

import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.api.ClientService
import com.hana.fieldmate.data.remote.model.request.CreateClientReq
import com.hana.fieldmate.data.remote.model.request.UpdateClientReq
import com.hana.fieldmate.data.remote.model.response.*
import com.hana.fieldmate.network.OrderQuery
import com.hana.fieldmate.network.SortQuery
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
            emit(ResultWrapper.Error(it.message!!))
        }
    }.flowOn(ioDispatcher)

    fun fetchClientById(clientId: Long): Flow<ResultWrapper<ClientRes>> = flow {
        clientService.fetchClientById(clientId).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.message!!))
        }
    }.flowOn(ioDispatcher)

    fun updateClient(
        clientId: Long,
        updateClientReq: UpdateClientReq
    ): Flow<ResultWrapper<UpdateClientRes>> = flow {
        clientService.updateClient(clientId, updateClientReq).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.message!!))
        }
    }.flowOn(ioDispatcher)

    fun deleteClient(clientId: Long): Flow<ResultWrapper<DeleteClientRes>> = flow {
        clientService.deleteClient(clientId).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.message!!))
        }
    }.flowOn(ioDispatcher)

    fun fetchClientList(
        companyId: Long,
        name: String?,
        sort: SortQuery?,
        order: OrderQuery?,
    ): Flow<ResultWrapper<ClientListRes>> = flow {
        clientService.fetchClientList(companyId, name, sort, order).onSuccess {
            emit(ResultWrapper.Success(it))
        }.onFailure {
            emit(ResultWrapper.Error(it.message!!))
        }
    }.flowOn(ioDispatcher)
}