package com.hana.umuljeong.data.model.request

import com.google.gson.annotations.SerializedName

data class CreateCompanyReq(
    @SerializedName("name")
    val name: String
)
