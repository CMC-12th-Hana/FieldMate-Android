package com.hana.fieldmate.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class ErrorRes(
    @SerializedName("errorCode")
    val errorCode: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("cause")
    val cause: String
)
