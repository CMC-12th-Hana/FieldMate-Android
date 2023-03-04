package com.hana.umuljeong.domain.model

import com.hana.umuljeong.getCurrentTime
import com.hana.umuljeong.ui.component.imagepicker.ImageInfo

data class ReportEntity(
    val id: Long,
    val client: String,
    val name: String,
    val category: String,
    val date: String = getCurrentTime(),
    val content: String,
    val images: List<ImageInfo> = emptyList()
)
