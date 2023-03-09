package com.hana.fieldmate.data

import com.hana.fieldmate.R
import com.hana.fieldmate.data.local.fakeMemberDataSource
import com.hana.fieldmate.data.remote.model.response.*
import com.hana.fieldmate.domain.model.BusinessEntity
import com.hana.fieldmate.domain.model.CategoryEntity
import com.hana.fieldmate.domain.model.ClientEntity
import com.hana.fieldmate.domain.model.MemberEntity
import com.hana.fieldmate.toLocalDate

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

fun BusinessRes.toBusinessEntity(): BusinessEntity {
    return BusinessEntity(
        id = -1L,
        name = this.name,
        startDate = this.businessPeriod.start.toLocalDate(),
        endDate = this.businessPeriod.finish.toLocalDate(),
        memberEntities = fakeMemberDataSource,
        description = this.description,
        revenue = this.revenue.toString()
    )
}

fun MemberRes.toMemberEntity(): MemberEntity {
    return MemberEntity(
        id = -1L,
        name = this.name,
        profileImg = R.drawable.ic_member_profile,
        company = this.companyName,
        phoneNumber = this.phoneNumber,
        staffRank = this.staffRank,
        staffNumber = this.staffNumber
    )
}

fun MemberListRes.toMemberEntityList(): List<MemberEntity> {
    val memberEntityList = mutableListOf<MemberEntity>()

    for (member in this.memberList) {
        memberEntityList.add(member.toMemberEntity())
    }

    return memberEntityList
}