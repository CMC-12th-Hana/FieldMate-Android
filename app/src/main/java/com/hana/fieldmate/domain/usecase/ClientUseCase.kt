package com.hana.fieldmate.domain.usecase

import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.model.request.UpdateClientReq
import com.hana.fieldmate.data.remote.model.response.ClientListRes
import com.hana.fieldmate.data.remote.model.response.ClientRes
import com.hana.fieldmate.data.remote.model.response.CreateClientRes
import com.hana.fieldmate.data.remote.model.response.UpdateClientRes
import com.hana.fieldmate.data.remote.repository.ClientRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateClientUseCase @Inject constructor(
    private val clientRepository: ClientRepository
) {
    operator fun invoke(
        companyId: Long,
        name: String,
        tel: String,
        srName: String,
        srPhoneNumber: String,
        srDepartment: String
    ): Flow<ResultWrapper<CreateClientRes>> =
        clientRepository.createClient(companyId, name, tel, srName, srPhoneNumber, srDepartment)
}

class FetchClientByIdUseCase @Inject constructor(
    private val clientRepository: ClientRepository
) {
    operator fun invoke(clientId: Long): Flow<ResultWrapper<ClientRes>> =
        clientRepository.fetchClientById(clientId)
}

class UpdateClientUseCase @Inject constructor(
    private val clientRepository: ClientRepository
) {
    operator fun invoke(
        clientId: Long,
        updateClientReq: UpdateClientReq
    ): Flow<ResultWrapper<UpdateClientRes>> =
        clientRepository.updateClient(clientId, updateClientReq)
}

class FetchClientListUseCase @Inject constructor(
    private val clientRepository: ClientRepository
) {
    operator fun invoke(companyId: Long): Flow<ResultWrapper<ClientListRes>> =
        clientRepository.fetchClientList(companyId)
}