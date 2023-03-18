package com.hana.fieldmate.domain.model

import androidx.compose.ui.graphics.Color
import com.hana.fieldmate.ui.component.imagepicker.ImageInfo
import com.hana.fieldmate.util.DateUtil.getCurrentTime

data class TaskEntity(
    val id: Long,
    val authorId: Long,
    val categoryId: Long,
    val clientId: Long,
    val businessId: Long,
    val author: String,
    val client: String,
    val business: String,
    val title: String,
    val category: String,
    val categoryColor: Color,
    val date: String = getCurrentTime(),
    val description: String,
    val images: List<ImageInfo> = emptyList()
)
