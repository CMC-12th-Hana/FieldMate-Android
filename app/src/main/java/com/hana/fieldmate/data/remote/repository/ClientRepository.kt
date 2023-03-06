package com.hana.fieldmate.data.remote.repository

import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.datasource.ClientDataSource
import com.hana.fieldmate.data.remote.model.request.CreateClientReq
import com.hana.fieldmate.data.remote.model.request.SalesRepresentative
import com.hana.fieldmate.data.remote.model.request.UpdateClientReq
import com.hana.fieldmate.data.remote.model.response.ClientListRes
import com.hana.fieldmate.data.remote.model.response.ClientRes
import com.hana.fieldmate.data.remote.model.response.CreateClientRes
import com.hana.fieldmate.data.remote.model.response.UpdateClientRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ClientRepository @Inject constructor(
    private val clientDataSource: ClientDataSource
) {
    fun createClient(
        companyId: Long,
        name: String,
        tel: String,
        srName: String,
        srPhoneNumber: String,
        srDepartment: String
    ): Flow<ResultWrapper<CreateClientRes>> =
        clientDataSource.createClient(
            companyId,
            CreateClientReq(
                name, tel, SalesRepresentative(
                    srName,
                    srPhoneNumber,
                    srDepartment
                )
            )
        )

    fun fetchClientById(clientId: Long): Flow<ResultWrapper<ClientRes>> =
        clientDataSource.fetchClientById(clientId)

    fun updateClient(
        clientId: Long,
        updateClientReq: UpdateClientReq
    ): Flow<ResultWrapper<UpdateClientRes>> =
        clientDataSource.updateClient(clientId, updateClientReq)

    fun fetchClientList(companyId: Long): Flow<ResultWrapper<ClientListRes>> =
        clientDataSource.fetchClientList(companyId)
}