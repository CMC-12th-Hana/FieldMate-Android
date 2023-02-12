package com.hana.umuljeong.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    defaultFontFamily = Pretendard,
    body1 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    button = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )

    /* Other default text styles to override
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)