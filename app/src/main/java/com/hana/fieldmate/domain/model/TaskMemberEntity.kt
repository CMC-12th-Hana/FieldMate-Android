package com.hana.fieldmate.domain.model

data class TaskMemberEntity(
    val memberName: String,
    val taskEntityList: List<TaskEntity>,
    val count: Int
)
