package com.hana.fieldmate.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hana.fieldmate.R
import com.hana.fieldmate.ui.theme.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields

enum class DateSelectionMode { START, END }

@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate?,
    startDate: LocalDate? = null,
    endDate: LocalDate? = null,
    currentYearMonth: YearMonth = YearMonth.from(LocalDate.now()),
    onYearMonthChanged: (YearMonth) -> Unit,
    eventList: List<LocalDate> = emptyList(),
    onDayClicked: (LocalDate) -> Unit
) {
    var currentMonth by rememberSaveable { mutableStateOf(currentYearMonth) }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MonthSelector(
            currentMonth = currentMonth,
            prevBtnOnClick = {
                currentMonth = currentMonth.minusMonths(1)
                onYearMonthChanged(currentMonth)
            },
            nextBtnOnClick = {
                currentMonth = currentMonth.plusMonths(1)
                onYearMonthChanged(currentMonth)
            }
        )

        Spacer(modifier = Modifier.height(25.dp))

        WeekHeader()

        for (num: Long in 0L until currentMonth.getNumberWeeks()) {
            Week(
                weekNumber = num,
                currentMonth = currentMonth,
                selectedDate = selectedDate,
                startDate = startDate,
                endDate = endDate,
                eventList = eventList,
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
        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
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
                style = Typography.title2
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
                    style = Typography.body3,
                    color = fontColor
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
    selectedDate: LocalDate?,
    startDate: LocalDate?,
    endDate: LocalDate?,
    eventList: List<LocalDate>,
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
                    else -> Font191919
                }

                val selectable = if (startDate != null) {
                    startDate <= currentDay
                } else if (endDate != null) {
                    currentDay <= endDate
                } else {
                    true
                }

                Day(
                    day = currentDay,
                    fontColor = fontColor,
                    selectable = selectable,
                    selectedDate = selectedDate,
                    hasEvent = eventList.contains(currentDay),
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
    selectable: Boolean,
    selectedDate: LocalDate?,
    hasEvent: Boolean = false,
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
                        shape = RoundedCornerShape(8.dp),
                        color = if (selected) Main356DF8 else Color.Transparent
                    )
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = { if (selectable) onDayClicked(day) }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day.dayOfMonth.toString(),
                    style = Typography.body3,
                    color = if (selected) Color.White else if (!selectable) FontDBDBDB else fontColor
                )
                if (hasEvent) {
                    Column(modifier = Modifier.fillMaxHeight()) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
                        )

                        Spacer(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Main356DF8, CircleShape)
                        )
                    }
                }
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
    FieldMateTheme {
        DatePicker(selectedDate = LocalDate.now(), onDayClicked = { }, onYearMonthChanged = { })
    }
}