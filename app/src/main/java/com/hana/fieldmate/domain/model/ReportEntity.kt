package com.hana.fieldmate.domain.model

import com.hana.fieldmate.getCurrentTime
import com.hana.fieldmate.ui.component.imagepicker.ImageInfo

data class TaskEntity(
    val id: Long,
    val client: String,
    val business: String,
    val title: String,
    val memberId: Long,
    val category: String,
    val date: String = getCurrentTime(),
    val content: String,
    val images: List<ImageInfo> = emptyList()
)
