package com.hana.fieldmate.data

import com.hana.fieldmate.data.remote.model.response.ClientListRes
import com.hana.fieldmate.data.remote.model.response.ClientRes
import com.hana.fieldmate.data.remote.model.response.TaskCategoryListRes
import com.hana.fieldmate.data.remote.model.response.TaskCategoryRes
import com.hana.fieldmate.domain.model.CategoryEntity
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

fun TaskCategoryRes.toCategoryEntity(): CategoryEntity {
    return CategoryEntity(
        id = this.categoryId,
        name = this.name,
        color = this.color
    )
}

fun TaskCategoryListRes.toCategoryEntityList(): List<CategoryEntity> {
    val categoryEntityList = mutableListOf<CategoryEntity>()

    for (category in this.categoryList) {
        categoryEntityList.add(category.toCategoryEntity())
    }

    return categoryEntityList
}
