package com.hana.umuljeong.data.model.response

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class CategoryRes(
    @SerializedName("taskCategoryId")
    val categoryId: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("color")
    val color: String
)

data class CategoryListRes(
    @SerializedName("taskCategoryDtoList")
    val categoryList: List<CategoryRes>,
    @SerializedName("count")
    val count: Int
)

data class CreateCategoryRes(
    @SerializedName("taskCategoryId")
    val categoryId: Long,
    @SerializedName("createdAt")
    val createdAt: LocalDateTime
)

data class UpdateCategoryRes(
    @SerializedName("taskCategoryId")
    val categoryId: Long,
    @SerializedName("updatedAt")
    val updateAt: LocalDateTime
)

data class DeleteCategoryListRes(
    @SerializedName("deletedAt")
    val deletedAt: LocalDateTime
)

data class UpdateCategoryListRes(
    @SerializedName("updatedTaskCategoryDtoList")
    val updateCategoryList: List<UpdateCategoryRes>,
    @SerializedName("count")
    val count: Int,
    @SerializedName("updatedAt")
    val updatedAt: LocalDateTime
)