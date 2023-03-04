package com.hana.umuljeong.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class CreateTaskCategoryReq(
    @SerializedName("name")
    val name: String,
    @SerializedName("color")
    val color: String
)

data class UpdateTaskCategoryReq(
    @SerializedName("name")
    val name: String,
    @SerializedName("color")
    val color: String
)

data class UpdateTaskCategoryListReq(
    @SerializedName("updateTaskCategoryDtoList")
    val categoryList: List<UpdateTaskCategoryReq>
)

data class DeleteTaskCategoryListReq(
    @SerializedName("taskCategoryIdList")
    val categoryIdList: List<Long>
)