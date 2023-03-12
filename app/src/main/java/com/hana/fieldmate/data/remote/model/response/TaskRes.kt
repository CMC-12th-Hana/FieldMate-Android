package com.hana.fieldmate.data.remote.model.response

import com.google.gson.annotations.SerializedName

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
    @SerializedName("taskCategoryColor")
    val categoryColor: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("taskImageDtoList")
    val taskImageList: List<TaskImageRes>
)

data class TaskImageRes(
    @SerializedName("taskImageId")
    val id: Long,
    @SerializedName("url")
    val url: String
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
    @SerializedName("memberDtoList")
    val memberTaskList: List<MemberTaskRes> = emptyList(),
    @SerializedName("taskDtoList")
    val taskList: List<TaskRes> = emptyList(),
    @SerializedName("count")
    val count: Int
)

/*
data class LeaderTaskListRes(
    @SerializedName("memberDtoList")
    val memberTaskList: List<MemberTaskRes>,
    @SerializedName("count")
    override val count: Int
): TaskListRes(count)

data class StaffTaskListRes(
    @SerializedName("taskDtoList")
    val taskList: List<TaskRes>,
    @SerializedName("count")
    override val count: Int
): TaskListRes(count)
 */

data class CreateTaskRes(
    @SerializedName("taskId")
    val taskId: Long,
    @SerializedName("createdAt")
    val createdAt: String
)