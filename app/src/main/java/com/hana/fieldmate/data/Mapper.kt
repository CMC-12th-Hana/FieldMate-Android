package com.hana.fieldmate.data

import com.hana.fieldmate.R
import com.hana.fieldmate.data.remote.model.response.*
import com.hana.fieldmate.domain.model.*
import com.hana.fieldmate.toColor
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
        id = this.businessId,
        name = this.name,
        startDate = this.businessPeriod.start.toLocalDate(),
        endDate = this.businessPeriod.finish.toLocalDate(),
        memberEntities = this.memberDtoList.toMemberNameEntityList(),
        description = this.description,
        revenue = this.revenue.toString()
    )
}

fun MemberRes.toMemberEntity(): MemberEntity {
    return MemberEntity(
        id = this.memberId,
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

fun MemberNameRes.toMemberNameEntity(): MemberNameEntity {
    return MemberNameEntity(
        id = this.id,
        name = this.name
    )
}

fun List<MemberNameRes>.toMemberNameEntityList(): List<MemberNameEntity> {
    val memberNameEntityList = mutableListOf<MemberNameEntity>()

    for (member in this) {
        memberNameEntityList.add(member.toMemberNameEntity())
    }

    return memberNameEntityList
}

fun MemberEntity.toMemberNameEntity(): MemberNameEntity {
    return MemberNameEntity(
        id = this.id,
        name = this.name
    )
}

fun List<MemberEntity>.toMemberNameEntities(): List<MemberNameEntity> {
    val memberNameEntityList = mutableListOf<MemberNameEntity>()

    for (member in this) {
        memberNameEntityList.add(member.toMemberNameEntity())
    }

    return memberNameEntityList
}

fun TaskRes.toTaskEntity(): TaskEntity {
    return TaskEntity(
        id = this.taskId,
        client = this.clientName,
        business = this.businessName,
        title = this.title,
        category = this.category,
        categoryColor = this.categoryColor.toColor(),
        description = this.description
    )
}

fun List<TaskRes>.toTaskEntityList(): List<TaskEntity> {
    val taskEntityList = mutableListOf<TaskEntity>()

    for (task in this) {
        taskEntityList.add(task.toTaskEntity())
    }

    return taskEntityList
}