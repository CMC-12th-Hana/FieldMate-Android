package com.hana.umuljeong.data.model.request

import com.google.gson.annotations.SerializedName

data class CreateCategoryReq(
    @SerializedName("name")
    val name: String,
    @SerializedName("color")
    val color: String
)

data class UpdateCategoryReq(
    @SerializedName("name")
    val name: String,
    @SerializedName("color")
    val color: String
)

data class UpdateCategoryListReq(
    @SerializedName("updateTaskCategoryDtoList")
    val categoryList: List<UpdateCategoryReq>
)

data class DeleteCategoryListReq(
    @SerializedName("taskCategoryIdList")
    val categoryIdList: List<Long>
)