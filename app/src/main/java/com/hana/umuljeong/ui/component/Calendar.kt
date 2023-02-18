package com.hana.umuljeong.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hana.umuljeong.ui.theme.FontDBDBDB
import com.hana.umuljeong.ui.theme.Main356DF8
import com.hana.umuljeong.ui.theme.Typography
import com.hana.umuljeong.ui.theme.UmuljeongTheme
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun UHorizontalCalendar(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate = LocalDate.now(),
    onDayClicked: (LocalDate) -> Unit
) {
    val scrollState = rememberLazyListState()

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        val currentYearMonth = YearMonth.from(selectedDate)
        var currentDay = currentYearMonth.atDay(1)
        val days = mutableListOf<LocalDate>()
        for (i: Int in 1..currentYearMonth.lengthOfMonth()) {
            days.add(currentDay)
            currentDay = currentDay.plusDays(1)
        }

        LazyRow(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            state = scrollState
        ) {
            items(days) { today ->
                Day(
                    day = today,
                    selectedDate = selectedDate,
                    onDayClicked = onDayClicked
                )
            }
        }

        LaunchedEffect(selectedDate) {
            val scrollPosition =
                if (selectedDate.dayOfMonth - 4 < 0) 0 else selectedDate.dayOfMonth - 4

            scrollState.animateScrollToItem(scrollPosition)
        }
    }

}

@Composable
private fun Day(
    modifier: Modifier = Modifier,
    day: LocalDate,
    selectedDate: LocalDate,
    onDayClicked: (LocalDate) -> Unit
) {
    val selected = (day == selectedDate)

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = weekdays[day.dayOfWeek.value - 1], style = Typography.body2)

            Spacer(modifier = Modifier.height(15.dp))

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        shape = RoundedCornerShape(2.dp),
                        color = if (selected) Main356DF8 else Color.Transparent
                    )
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = { onDayClicked(day) }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day.dayOfMonth.toString(),
                    style = Typography.body2,
                    color = if (selected) Color.White else FontDBDBDB
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewHorizontalCalendar() {
    UmuljeongTheme {
        UHorizontalCalendar(onDayClicked = { })
    }
}

val weekdays = listOf("월", "화", "수", "목", "금", "토", "일")