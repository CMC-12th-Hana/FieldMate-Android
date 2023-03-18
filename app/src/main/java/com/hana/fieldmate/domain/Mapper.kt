package com.hana.fieldmate.domain

import android.net.Uri
import com.hana.fieldmate.R
import com.hana.fieldmate.data.remote.model.response.*
import com.hana.fieldmate.domain.model.*
import com.hana.fieldmate.ui.component.imagepicker.ImageInfo
import com.hana.fieldmate.ui.theme.CategoryColor
import com.hana.fieldmate.util.DateUtil.toLocalDate
import com.hana.fieldmate.util.StringUtil.toColor
import java.time.LocalDate
import java.util.*

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
        clientName = this.clientName,
        startDate = this.businessPeriod.start.toLocalDate(),
        endDate = this.businessPeriod.finish.toLocalDate(),
        memberEntities = this.memberDtoList.toMemberNameEntityList(),
        description = this.description,
        revenue = this.revenue.toString()
    )
}

fun BusinessListRes.toBusinessEntityList(): List<BusinessEntity> {
    val businessEntityList = mutableListOf<BusinessEntity>()

    for (business in this.businessDtoList) {
        businessEntityList.add(business.toBusinessEntity())
    }

    return businessEntityList
}

fun MemberRes.toMemberEntity(): MemberEntity {
    return MemberEntity(
        id = this.memberId,
        name = this.name,
        role = this.role,
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
        authorId = this.memberId,
        categoryId = this.categoryId,
        clientId = this.clientId,
        businessId = this.businessId,
        author = this.memberName,
        client = this.clientName,
        business = this.businessName,
        title = this.title,
        category = this.category,
        categoryColor = if (this.categoryColor == "") CategoryColor[0] else this.categoryColor.toColor(),
        date = this.date,
        description = this.description,
        images = this.taskImageList.map {
            ImageInfo(it.id, "", Date(), Uri.parse(it.url))
        }
    )
}

fun MemberNameEntity.toMemberEntity(): MemberEntity {
    return MemberEntity(
        id = this.id,
        profileImg = R.drawable.ic_member_profile,
        name = this.name,
        role = "",
        company = "",
        phoneNumber = "",
        staffRank = "",
        staffNumber = ""
    )
}

fun TaskStatisticRes.toTaskStatisticEntity(): TaskStatisticEntity {
    return TaskStatisticEntity(
        name = this.name,
        color = this.color.toColor(),
        count = this.count
    )
}

fun TaskStatisticListRes.toTaskStatisticList(): List<TaskStatisticEntity> {
    val taskStatisticEntityList = mutableListOf<TaskStatisticEntity>()

    for (task in this.taskStatisticList) {
        taskStatisticEntityList.add(task.toTaskStatisticEntity())
    }

    return taskStatisticEntityList
}

fun MemberTaskRes.toTaskMemberEntity(): TaskMemberEntity {
    return TaskMemberEntity(
        memberName = this.name,
        taskEntityList = this.taskDtoList.toTaskEntityList(),
        count = this.count
    )
}

fun List<TaskRes>.toTaskEntityList(): List<TaskEntity> {
    val taskEntityList = mutableListOf<TaskEntity>()

    for (task in this) {
        taskEntityList.add(task.toTaskEntity())
    }

    return taskEntityList
}

fun List<TaskRes>.toLocalDateList(): List<LocalDate> {
    val dateSet = mutableSetOf<LocalDate>()

    for (task in this) {
        dateSet.add(task.date.toLocalDate())
    }

    return dateSet.toList()
}

fun List<MemberTaskRes>.toTaskMemberEntityList(): List<TaskMemberEntity> {
    val taskMemberEntityList = mutableListOf<TaskMemberEntity>()

    for (task in this) {
        taskMemberEntityList.add(task.toTaskMemberEntity())
    }

    return taskMemberEntityList
}