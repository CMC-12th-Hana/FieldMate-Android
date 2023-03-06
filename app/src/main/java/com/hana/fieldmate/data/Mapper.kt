package com.hana.fieldmate.data

import com.hana.fieldmate.data.remote.model.response.ClientListRes
import com.hana.fieldmate.data.remote.model.response.ClientRes
import com.hana.fieldmate.domain.model.ClientEntity

fun ClientRes.toClientEntity(): ClientEntity {
    return ClientEntity(
        id = this.clientId,
        name = this.name,
        phone = this.tel,
        salesRepresentativeName = this.salesRepresentative.name,
        salesRepresentativePhone = this.salesRepresentative.phoneNumber,
        salesRepresentativeDepartment = this.salesRepresentative.department,
        taskCount = this.taskCount,
        businessCount = this.businessCount
    )
}


fun ClientListRes.toClientEntityList(): List<ClientEntity> {
    val clientEntityList = mutableListOf<ClientEntity>()

    for (client in this.clientList) {
        clientEntityList.add(client.toClientEntity())
    }

    return clientEntityList
}
