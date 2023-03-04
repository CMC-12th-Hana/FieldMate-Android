package com.hana.umuljeong.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class CreateCompanyReq(
    @SerializedName("name")
    val name: String
)
