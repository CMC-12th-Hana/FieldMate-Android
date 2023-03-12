package com.hana.fieldmate.domain.model

import androidx.compose.ui.graphics.Color
import com.hana.fieldmate.getCurrentTime
import com.hana.fieldmate.ui.component.imagepicker.ImageInfo

data class TaskEntity(
    val id: Long,
    val client: String,
    val business: String,
    val title: String,
    val category: String,
    val categoryColor: Color,
    val date: String = getCurrentTime(),
    val description: String,
    val images: List<ImageInfo> = emptyList()
)
