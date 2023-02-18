package com.hana.umuljeong.ui.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// 피그마 디자인 기준으로 body가 title h가 body
val Typography = Typography(
    defaultFontFamily = Pretendard,
    body1 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = Font191919
    ),
    body2 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        color = Font191919
    )

    /* Other default text styles to override
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)

val Typography.title1: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Font191919
        )
    }

val Typography.title2: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Font191919
        )
    }

val Typography.body3: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = Font191919
        )
    }

val Typography.body4: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = Font191919
        )
    }

val Typography.body5: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp,
            color = Font191919
        )
    }

val Typography.body6: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontFamily = Pretendard,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            color = Font191919
        )
    }