package com.hana.fieldmate.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class CreateCompanyReq(
    @SerializedName("name")
    val name: String
)
