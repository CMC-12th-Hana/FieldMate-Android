package com.hana.umuljeong.data.remote.model.request

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import java.time.LocalDate

data class CreateTaskReq(
    @SerializedName("clientCompanyId")
    val clientId: Long,
    @SerializedName("taskCategoryId")
    val categoryId: Long,
    @SerializedName("date")
    val date: LocalDate,
    @SerializedName("description")
    val description: String,
    @SerializedName("taskImageList")
    val imageList: List<MultipartBody.Part>
)