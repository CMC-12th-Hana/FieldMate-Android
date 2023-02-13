package com.hana.umuljeong.ui.component.imagepicker

import android.net.Uri
import java.util.*

data class ImageInfo(
    val id: Long,
    val displayName: String,
    val dateTaken: Date,
    val contentUri: Uri
)
