package com.hana.fieldmate.data.remote.model.response

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class TaskRes(
    @SerializedName("taskId")
    val taskId: Long,
    @SerializedName("businessName")
    val businessName: String,
    @SerializedName("clientName")
    val clientName: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("taskCategory")
    val category: String,
    @SerializedName("date")
    val date: LocalDate
)

data class MemberTaskRes(
    @SerializedName("name")
    val name: String,
    @SerializedName("taskDtoList")
    val taskDtoList: List<TaskRes>,
    @SerializedName("count")
    val count: Int
)

data class TaskListRes(
    @SerializedName("count")
    val count: Int
)

data class LeaderTaskListRes(
    @SerializedName("memberDtoList")
    val memberTaskList: List<MemberTaskRes>
)

data class StaffTaskListRes(
    @SerializedName("taskDtoList")
    val taskList: List<TaskRes>
)

data class CreateTaskRes(
    @SerializedName("taskId")
    val taskId: Long,
    @SerializedName("createdAt")
    val createdAt: String
)