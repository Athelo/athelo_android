@file:OptIn(ExperimentalAnimationApi::class)

package com.i2asolutions.athelo.presentation.ui.base.calendar

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.model.calendar.Day
import com.i2asolutions.athelo.presentation.model.calendar.DayRange
import com.i2asolutions.athelo.presentation.model.calendar.createDayRange
import com.i2asolutions.athelo.presentation.model.calendar.toDay
import com.i2asolutions.athelo.presentation.ui.theme.*
import kotlinx.coroutines.runBlocking
import java.util.*

@Composable
fun SimpleDayPicker(
    modifier: Modifier,
    initDay: Day,
    selectedDay: Day,
    maxDay: Day? = null,
    minDay: Day? = null,
    onDayClick: (Day) -> Unit = {},
    showArrows: Boolean = true,
) {
    Column(modifier = modifier) {
        var selectedDate by remember { mutableStateOf(selectedDay) }
        var currentDate by remember { mutableStateOf(Date()) }

        var daysRange by remember {
            mutableStateOf(createDayRange(Calendar.getInstance().apply {
                set(initDay.year, initDay.month.value - 1, initDay.day, 12, 0, 0)
                add(Calendar.DAY_OF_YEAR, -6)
            }.also { currentDate = it.time }))
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Column {
                Text(
                    text = selectedDate.fullName,
                    style = MaterialTheme.typography.body1.copy(
                        color = gray,
                        textAlign = TextAlign.Start
                    ),
                )
                Text(
                    text = if (selectedDate.isToday) stringResource(id = R.string.Today) else selectedDate.weekName,
                    style = MaterialTheme.typography.headline20.copy(
                        color = black,
                        textAlign = TextAlign.Start
                    ),
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            if (showArrows) {
                var firstArrowAvailable by remember {
                    mutableStateOf(checkMinDayCondition(minDay, daysRange))
                }
                var lastArrowAvailable by remember {
                    mutableStateOf(checkMaxDayCondition(maxDay, daysRange))
                }
                var lastClickTimestamp by remember { mutableStateOf(0L) }
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_prev),
                    contentDescription = "Arrow Prev",
                    modifier = Modifier
                        .padding(16.dp)
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (firstArrowAvailable) white else white.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            if (1000L + lastClickTimestamp < System.currentTimeMillis()) {
                                lastClickTimestamp = System.currentTimeMillis()
                                if (firstArrowAvailable) {
                                    daysRange = createDayRange(
                                        Calendar
                                            .getInstance()
                                            .apply {
                                                time = currentDate
                                                add(Calendar.DAY_OF_YEAR, -7)
                                            }
                                            .also { currentDate = it.time }).also { range ->
                                        lastArrowAvailable = checkMaxDayCondition(maxDay, range)
                                        firstArrowAvailable = checkMinDayCondition(minDay, range)
                                    }
                                }
                            }
                        },
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_next),
                    colorFilter = ColorFilter.tint(calendarArrow.copy(alpha = if (lastArrowAvailable) 1.0f else 0.5f)),
                    contentDescription = "Arrow Next",
                    modifier = Modifier
                        .padding(16.dp)
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (firstArrowAvailable) white else white.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            runBlocking {
                                if (1000L + lastClickTimestamp < System.currentTimeMillis()) {
                                    lastClickTimestamp = System.currentTimeMillis()
                                    if (lastArrowAvailable) {
                                        daysRange = createDayRange(
                                            Calendar
                                                .getInstance()
                                                .apply {
                                                    time = currentDate
                                                    add(Calendar.DAY_OF_YEAR, 7)
                                                }
                                                .also { currentDate = it.time }).also { range ->
                                            lastArrowAvailable = checkMaxDayCondition(maxDay, range)
                                            firstArrowAvailable =
                                                checkMinDayCondition(minDay, range)
                                        }
                                    }
                                }
                            }
                        },
                )
            }
        }
        AnimatedContent(targetState = daysRange, transitionSpec = {
            if (targetState.first() > initialState.first()) {
                slideInHorizontally(animationSpec = tween(300)) { width -> width } with
                        slideOutHorizontally(animationSpec = tween(300)) { width -> -width }
            } else {
                slideInHorizontally(animationSpec = tween(300)) { width -> -width } with
                        slideOutHorizontally(animationSpec = tween(300)) { width -> width }
            }.using(SizeTransform(clip = false))
        }) { targetRange ->
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                targetRange.forEach {
                    DayCell(modifier = Modifier,
                        day = it,
                        selected = {
                            selectedDate == it
                        },
                        enabled = {
                            (minDay?.let { minDay -> it >= minDay } ?: true && maxDay?.let { maxDay -> it <= maxDay } ?: true)
                        },
                        onItemClick = { day ->
                            selectedDate = day
                            onDayClick(day)
                        })
                }
            }
        }
    }
}

@Composable
private fun RowScope.DayCell(
    day: Day,
    selected: () -> Boolean,
    enabled: () -> Boolean,
    onItemClick: (Day) -> Unit,
    modifier: Modifier
) {
    val bgShape = RoundedCornerShape(16.dp)
    Column(
        modifier = modifier
            .clip(bgShape)
            .background(
                if (selected()) lightPurple.copy(alpha = 0.17f) else Color.Transparent,
                shape = bgShape
            )
            .weight(1f)
            .clickable {
                if (enabled())
                    onItemClick(day)
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = day.day.toString(),
            style = MaterialTheme.typography.button.copy(
                if (selected()) darkPurple else black.copy(
                    alpha = if (enabled()) 1f else 0.5f
                )
            ),
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(top = 10.dp)
        )
        Text(
            text = day.shortWeekName,
            style = MaterialTheme.typography.button.copy(
                if (selected()) darkPurple else cyanBlue.copy(
                    alpha = if (enabled()) 1f else 0.5f
                )
            ),
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .padding(bottom = 10.dp)
        )
    }
}

private fun checkMaxDayCondition(
    maxDay: Day?,
    range: DayRange
) = maxDay?.let {
    range
        .lastOrNull()
        ?.let { lastDay -> lastDay < maxDay } ?: false
} ?: true

private fun checkMinDayCondition(
    minDay: Day?,
    range: DayRange
) = minDay?.let {
    range
        .firstOrNull()
        ?.let { firstDay -> firstDay > minDay } ?: false
} ?: true


@Preview(backgroundColor = 0xFFb4b4b4)
@Composable
fun CalendarPreview() {
    MaterialTheme {
        SimpleDayPicker(
            modifier = Modifier.fillMaxWidth(),
            initDay = Calendar.getInstance().toDay(),
            selectedDay = Calendar.getInstance().toDay(),
            minDay = Calendar.getInstance()
                .also { calendar -> calendar.add(Calendar.DAY_OF_YEAR, -30) }.toDay(),
            maxDay = Calendar.getInstance().toDay()
        )
    }
}