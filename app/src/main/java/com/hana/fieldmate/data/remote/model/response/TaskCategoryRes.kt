package com.hana.fieldmate.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class TaskCategoryRes(
    @SerializedName("taskCategoryId")
    val categoryId: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("color")
    val color: String
)

data class TaskCategoryListRes(
    @SerializedName("taskCategoryDtoList")
    val categoryList: List<TaskCategoryRes>,
    @SerializedName("count")
    val count: Int
)

data class CreateTaskCategoryRes(
    @SerializedName("taskCategoryId")
    val categoryId: Long,
    @SerializedName("createdAt")
    val createdAt: String
)

data class UpdateTaskCategoryRes(
    @SerializedName("taskCategoryId")
    val categoryId: Long,
    @SerializedName("updatedAt")
    val updateAt: String
)

data class DeleteTaskCategoryListRes(
    @SerializedName("deletedAt")
    val deletedAt: String
)

data class UpdateTaskCategoryListRes(
    @SerializedName("updatedTaskCategoryDtoList")
    val updateCategoryList: List<UpdateTaskCategoryRes>,
    @SerializedName("count")
    val count: Int,
    @SerializedName("updatedAt")
    val updatedAt: String
)