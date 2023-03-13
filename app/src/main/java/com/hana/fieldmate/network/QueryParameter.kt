package com.hana.fieldmate.network

enum class SortQuery(val value: String) {
    NAME("name"),
    TASK_COUNT("taskCount"),
    BUSINESS_COUNT("businessCount")
}

enum class OrderQuery(val value: String) {
    ASC("ASC"),
    DESC("DESC")
}

enum class TaskTypeQuery(val value: String) {
    MEMBER("MEMBER"),
    TASK("TASK")
}