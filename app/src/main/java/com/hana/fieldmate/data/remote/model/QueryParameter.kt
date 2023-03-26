package com.hana.fieldmate.data.remote.model

import com.google.gson.annotations.SerializedName

enum class SortQuery {
    @SerializedName("name")
    NAME,

    @SerializedName("taskCount")
    TASK_COUNT,

    @SerializedName("businessCount")
    BUSINESS_COUNT
}

enum class OrderQuery {
    @SerializedName("ASC")
    ASC,

    @SerializedName("DESC")
    DESC
}

enum class TaskTypeQuery {
    @SerializedName("MEMBER")
    MEMBER,

    @SerializedName("TASK")
    TASK
}