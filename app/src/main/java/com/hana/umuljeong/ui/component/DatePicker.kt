package com.hana.umuljeong.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hana.umuljeong.R
import com.hana.umuljeong.ui.theme.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields

@Composable
fun UDatePicker(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate,
    currentYearMonth: YearMonth = YearMonth.from(LocalDate.now()),
    onDayClicked: (LocalDate) -> Unit
) {
    var currentMonth by remember { mutableStateOf(currentYearMonth) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MonthSelector(
            currentMonth = currentMonth,
            prevBtnOnClick = { currentMonth = currentMonth.minusMonths(1) },
            nextBtnOnClick = { currentMonth = currentMonth.plusMonths(1) }
        )

        Spacer(modifier = Modifier.height(25.dp))

        WeekHeader()

        for (num: Long in 0L until currentMonth.getNumberWeeks()) {
            Week(
                weekNumber = num,
                currentMonth = currentMonth,
                selectedDate = selectedDate,
                onDayClicked = onDayClicked
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MonthSelector(
    modifier: Modifier = Modifier,
    currentMonth: YearMonth,
    prevBtnOnClick: () -> Unit,
    nextBtnOnClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
            IconButton(
                onClick = prevBtnOnClick
            ) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.ic_prev_month
                    ),
                    tint = Color.Unspecified,
                    contentDescription = null
                )
            }

            Text(
                text = "${currentMonth.year}년 ${currentMonth.monthValue}월",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = nextBtnOnClick
            ) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.ic_next_month
                    ),
                    tint = Color.Unspecified,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun WeekHeader(modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val weekdays = listOf("일", "월", "화", "수", "목", "금", "토")

        for (day in weekdays) {
            val fontColor = when (day) {
                "토" -> Main356DF8
                "일" -> ErrorFF3120
                else -> Font191919
            }

            Box(
                modifier = Modifier
                    .size(42.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = fontColor
                    )
                )
            }
        }
    }
}

@Composable
fun Week(
    modifier: Modifier = Modifier,
    weekNumber: Long,
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    onDayClicked: (LocalDate) -> Unit
) {
    val beginningWeek = currentMonth.atDay(1).plusWeeks(weekNumber)
    var currentDay = beginningWeek.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))

    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (i in 0..6) {
            if (currentDay.month == currentMonth.month) {
                val fontColor = when (i) {
                    0 -> ErrorFF3120
                    6 -> Main356DF8
                    else -> Font70747E
                }

                Day(
                    day = currentDay,
                    fontColor = fontColor,
                    selectedDate = selectedDate,
                    onDayClicked = onDayClicked
                )
            } else {
                Box(modifier = Modifier.size(42.dp))
            }
            currentDay = currentDay.plusDays(1)
        }
    }
}

@Composable
private fun Day(
    modifier: Modifier = Modifier,
    day: LocalDate,
    fontColor: Color,
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
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        shape = CircleShape,
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
                    color = if (selected) Color.White else fontColor
                )
            }
        }
    }
}

fun YearMonth.getNumberWeeks(weekFields: WeekFields = WeekFields.SUNDAY_START): Int {
    val firstWeekNumber = this.atDay(1)[weekFields.weekOfMonth()]
    val lastWeekNumber = this.atEndOfMonth()[weekFields.weekOfMonth()]

    return lastWeekNumber - firstWeekNumber + 1
}

@Preview
@Composable
fun PreviewDatePicker() {
    UmuljeongTheme {
        UDatePicker(selectedDate = LocalDate.now(), onDayClicked = { })
    }
}