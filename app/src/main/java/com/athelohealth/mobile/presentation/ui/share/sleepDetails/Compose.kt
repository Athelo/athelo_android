@file:OptIn(ExperimentalMaterial3Api::class)

package com.athelohealth.mobile.presentation.ui.share.sleepDetails

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.model.base.ChartCloudContent
import com.athelohealth.mobile.presentation.model.patients.Patient
import com.athelohealth.mobile.presentation.model.sleep.HistoryRange
import com.athelohealth.mobile.presentation.model.sleep.SleepDetailScreen
import com.athelohealth.mobile.presentation.ui.base.BoxScreen
import com.athelohealth.mobile.presentation.ui.base.RadioButton
import com.athelohealth.mobile.presentation.ui.base.RadioButtonGroup
import com.athelohealth.mobile.presentation.ui.base.ToolbarWithNameBack
import com.athelohealth.mobile.presentation.ui.base.chart.BarChart
import com.athelohealth.mobile.presentation.ui.caregiver.selectPatient.ChoosePatientPopup
import com.athelohealth.mobile.presentation.ui.caregiver.selectPatient.SelectPatientCell
import com.athelohealth.mobile.presentation.ui.share.sleepSummary.SleepSummaryEvent
import com.athelohealth.mobile.presentation.ui.theme.*
import com.athelohealth.mobile.utils.createMockDailySleepDetails
import com.athelohealth.mobile.utils.createMockMonthlySleepDetails
import com.athelohealth.mobile.utils.createMockPeriodInfo
import com.athelohealth.mobile.utils.createMockWeeklySleepDetails
import com.athelohealth.mobile.widgets.HtmlText

@Composable
fun SleepDetailsScreen(viewModel: SleepDetailsViewModel) {
    val viewState by viewModel.viewState.collectAsState()
    BoxScreen(
        viewModel = viewModel,
        showProgressProvider = { viewState.isLoading },
        includeStatusBarPadding = true
    ) {
        SleepDetailsScreen(
            handleEvent = viewModel::handleEvent,
            selectedRange = viewState.selectedRange,
            periodInfo = viewState.periodInfo,
            dailyInformation = viewState.dailyInformation,
            weeklyInformation = viewState.weeklyInformation,
            monthlyInformation = viewState.monthlyInformation,
            sleepDesc = viewState.sleepDesc,
            patients = viewState.patients,
            selectedPatient = viewState.selectedPatient,
        )
    }
}

@Composable
fun SleepDetailsScreen(
    handleEvent: (SleepDetailEvent) -> Unit,
    selectedRange: HistoryRange = HistoryRange.Day,
    periodInfo: SleepDetailScreen.PeriodInfo?,
    dailyInformation: SleepDetailScreen.DailyInformation?,
    weeklyInformation: SleepDetailScreen.WeeklyInformation?,
    monthlyInformation: SleepDetailScreen.MonthlyInformation?,
    sleepDesc: String? = null,
    selectedPatient: Patient?,
    patients: List<Patient>,
) {
    var showPatientChooser by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
    ) {
        ToolbarWithNameBack(
            backClick = { handleEvent(SleepDetailEvent.BackClick) },
            screenName = stringResource(id = R.string.Sleep)
        )
        if (selectedPatient != null)
            SelectPatientCell(patient = selectedPatient, onCellClick = {
                showPatientChooser = true
            })
        RadioButtonGroup(
            selectionProvider = { selectedRange.tab },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp),
            buttons = arrayOf(
                RadioButton(
                    includePadding = false,
                    text = stringResource(id = R.string.Day),
                    onClick = {
                        handleEvent(SleepDetailEvent.RangeChanged(HistoryRange.Day))
                    }
                ),
                RadioButton(
                    includePadding = false,
                    text = stringResource(id = R.string.Week),
                    onClick = {
                        handleEvent(SleepDetailEvent.RangeChanged(HistoryRange.Week))
                    }),
                RadioButton(
                    includePadding = false,
                    text = stringResource(id = R.string.Month),
                    onClick = {
                        handleEvent(SleepDetailEvent.RangeChanged(HistoryRange.Month))
                    })
            )
        )
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = false),
            onRefresh = { handleEvent(SleepDetailEvent.RefreshData) }) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                if (periodInfo != null) item {
                    PeriodInfoCell(periodInfo, handleEvent)
                }
                if (selectedRange == HistoryRange.Day && dailyInformation != null) item {
                    DailyCell(dailyInformation)
                }
                if (selectedRange == HistoryRange.Week && weeklyInformation != null) item {
                    WeeklyCell(weeklyInformation)
                }
                if (selectedRange == HistoryRange.Month && monthlyInformation != null) item {
                    MonthlyCell(monthlyInformation)
                }
                item {
                    SleepPhaseDescriptionCell()
                }
                if (!sleepDesc.isNullOrBlank()) item {
                    SleepQualityInfoCell(sleepDesc)
                }
                item {
                    Spacer(modifier = Modifier.navigationBarsPadding())
                }
            }
        }
    }
    if (showPatientChooser && patients.isNotEmpty()) {
        ChoosePatientPopup(
            patients,
            selectedPatient ?: patients.first(),
            onCancelClick = {
                showPatientChooser = false
            },
            onSwitchClick = { patient ->
                handleEvent(SleepDetailEvent.ChangePatient(patient))
                showPatientChooser = false
            })
    }
}

@Composable
fun DailyCell(info: SleepDetailScreen.DailyInformation) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .align(CenterVertically),
                text = stringResource(id = R.string.Daily_Overview),
                style = MaterialTheme.typography.headline20.copy(
                    color = gray,
                    textAlign = TextAlign.Left
                )
            )
            Text(
                modifier = Modifier
                    .align(CenterVertically),
                text = info.total,
                style = MaterialTheme.typography.bold20.copy(color = lightOlivaceous)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            SleepPhaseDayCell(
                modifier = Modifier.weight(1f),
                info.deepSleep,
                R.string.Deep_Sleep,
                paddingEnd = 12.dp
            )
            SleepPhaseDayCell(
                modifier = Modifier.weight(1f),
                info.rem,
                R.string.REM_Sleep,
                paddingStart = 12.dp
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            SleepPhaseDayCell(
                modifier = Modifier.weight(1f),
                info.lightSleep,
                R.string.Light_Sleep,
                paddingEnd = 12.dp
            )
            SleepPhaseDayCell(
                modifier = Modifier.weight(1f),
                info.awake,
                R.string.Awake,
                paddingStart = 12.dp
            )
        }
    }
}

@Composable
fun WeeklyCell(info: SleepDetailScreen.WeeklyInformation) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .align(CenterVertically),
                text = stringResource(id = R.string.Week_Overview),
                style = MaterialTheme.typography.headline20.copy(
                    color = gray,
                    textAlign = TextAlign.Left
                )
            )
            Text(
                modifier = Modifier
                    .align(CenterVertically),
                text = info.avgSleepValue,
                style = MaterialTheme.typography.bold20.copy(color = lightOlivaceous)
            )
        }
        Text(
            modifier = Modifier.padding(top = 15.dp, bottom = 5.dp),
            text = stringResource(id = R.string.Hours),
            style = MaterialTheme.typography.subtitle.copy(color = lightGray, fontSize = 10.sp)
        )
        BarChart(
            modifier = Modifier
                .height(330.dp),
            chartData = info.sleepDataSet
        ) { entry ->
            ChartCloudContent(
                Modifier.align(Alignment.Center),
                info.sleepDataSet.cloudFormatter(entry)
            )
        }

        Text(
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(top = 24.dp),
            text = info.avgSleepValue,
            style = MaterialTheme.typography.bold20.copy(color = lightOlivaceous)
        )

        Text(
            modifier = Modifier
                .align(CenterHorizontally),
            text = stringResource(id = R.string.Average_Sleep_Value),
            style = MaterialTheme.typography.body1.copy(color = gray)
        )

        FlowRow(
            modifier = Modifier.padding(top = 24.dp),
            mainAxisSize = SizeMode.Expand,
            mainAxisAlignment = MainAxisAlignment.SpaceBetween,
            mainAxisSpacing = 14.dp,
            crossAxisSpacing = 16.dp
        ) {
            Row {
                Box(
                    modifier = Modifier
                        .size(23.dp, 15.dp)
                        .align(CenterVertically)
                        .background(
                            colorResource(id = R.color.remSleepColor),
                            shape = RoundedCornerShape(5.dp)
                        )
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stringResource(id = R.string.REM_Sleep),
                    style = MaterialTheme.typography.body1.copy(color = gray)
                )
            }
            Row {
                Box(
                    modifier = Modifier
                        .size(23.dp, 15.dp)
                        .align(CenterVertically)
                        .background(
                            colorResource(id = R.color.deepSleepColor),
                            shape = RoundedCornerShape(5.dp)
                        )
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stringResource(id = R.string.Deep_Sleep),
                    style = MaterialTheme.typography.body1.copy(color = gray)
                )
            }
            Row {
                Box(
                    modifier = Modifier
                        .size(23.dp, 15.dp)
                        .align(CenterVertically)
                        .background(
                            colorResource(id = R.color.lightSleepColor),
                            shape = RoundedCornerShape(5.dp)
                        )
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stringResource(id = R.string.Light_Sleep),
                    style = MaterialTheme.typography.body1.copy(color = gray)
                )
            }
            Row {
                Box(
                    modifier = Modifier
                        .size(23.dp, 15.dp)
                        .align(CenterVertically)
                        .border(
                            width = 2.dp,
                            shape = RoundedCornerShape(5.dp),
                            brush = Brush.verticalGradient(
                                listOf(
                                    colorResource(id = R.color.totalSleepGradientStart),
                                    colorResource(id = R.color.totalSleepGradientEnd)
                                )
                            )
                        )
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stringResource(id = R.string.Total_Sleep),
                    style = MaterialTheme.typography.body1.copy(color = gray)
                )
            }
        }
    }
}

@Composable
fun MonthlyCell(info: SleepDetailScreen.MonthlyInformation) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .align(CenterVertically),
                text = stringResource(id = R.string.Month_Overview),
                style = MaterialTheme.typography.headline20.copy(
                    color = gray,
                    textAlign = TextAlign.Left
                )
            )
            Text(
                modifier = Modifier
                    .align(CenterVertically),
                text = info.avgSleepValue,
                style = MaterialTheme.typography.bold20.copy(color = lightOlivaceous)
            )
        }
        Text(
            modifier = Modifier.padding(top = 15.dp, bottom = 5.dp),
            text = stringResource(id = R.string.Hours),
            style = MaterialTheme.typography.subtitle.copy(color = lightGray, fontSize = 10.sp)
        )
        BarChart(
            modifier = Modifier
                .height(330.dp),
            chartData = info.sleepDataSet,
        ) { entry ->
            ChartCloudContent(
                Modifier.align(Alignment.Center),
                info.sleepDataSet.cloudFormatter(entry)
            )
        }

        Text(
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(top = 24.dp),
            text = info.avgSleepValue,
            style = MaterialTheme.typography.bold20.copy(color = lightOlivaceous)
        )

        Text(
            modifier = Modifier
                .align(CenterHorizontally),
            text = stringResource(id = R.string.Average_Sleep_Value),
            style = MaterialTheme.typography.body1.copy(color = gray)
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .padding(top = 24.dp),
                    text = info.deepSleepPercentage,
                    style = MaterialTheme.typography.bold20.copy(color = lightOlivaceous)
                )

                Text(
                    modifier = Modifier
                        .align(CenterHorizontally),
                    text = stringResource(id = R.string.Deep_Sleep),
                    style = MaterialTheme.typography.body1.copy(color = gray)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .padding(top = 24.dp),
                    text = info.remSleepPercentage,
                    style = MaterialTheme.typography.bold20.copy(color = lightOlivaceous)
                )

                Text(
                    modifier = Modifier
                        .align(CenterHorizontally),
                    text = stringResource(id = R.string.REM_Sleep),
                    style = MaterialTheme.typography.body1.copy(color = gray)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .padding(top = 24.dp),
                    text = info.lightSleepPercentage,
                    style = MaterialTheme.typography.bold20.copy(color = lightOlivaceous)
                )

                Text(
                    modifier = Modifier
                        .align(CenterHorizontally),
                    text = stringResource(id = R.string.Light_Sleep),
                    style = MaterialTheme.typography.body1.copy(color = gray)
                )
            }
        }
    }
}

@Composable
fun SleepPhaseDayCell(
    modifier: Modifier,
    value: String,
    @StringRes label: Int,
    paddingStart: Dp = 0.dp,
    paddingEnd: Dp = 0.dp
) {
    Card(
        modifier = modifier
            .padding(start = paddingStart, end = paddingEnd)
            .fillMaxWidth()
            .shadow(shape = RoundedCornerShape(20.dp), elevation = 5.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = white
        )
    ) {
        Box {
            Image(
                modifier = modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.ic_sleep_history_bg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alpha = 0.8f
            )
            Column(
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bold24.copy(color = lightOlivaceous)
                )
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(id = label),
                    style = MaterialTheme.typography.body1.copy(color = gray)
                )
            }
        }
    }
}

@Composable
fun PeriodInfoCell(
    periodInfo: SleepDetailScreen.PeriodInfo,
    handleEvent: (SleepDetailEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = periodInfo.date,
                style = MaterialTheme.typography.body1.copy(color = gray)
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = periodInfo.rangeName,
                style = MaterialTheme.typography.headline20.copy(color = black)
            )
        }
        Image(
            painter = painterResource(id = R.drawable.ic_arrow_prev),
            colorFilter = ColorFilter.tint(calendarArrow.copy(alpha = if (periodInfo.canGoBack) 1.0f else 0.5f)),
            contentDescription = "Arrow Previous",
            modifier = Modifier
                .align(CenterVertically)
                .padding(end = 16.dp)
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    if (periodInfo.canGoBack) white else white.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable {
                    handleEvent(SleepDetailEvent.PrevClicked)
                },
        )

        Image(
            painter = painterResource(id = R.drawable.ic_arrow_next),
            colorFilter = ColorFilter.tint(calendarArrow.copy(alpha = if (periodInfo.canGoForward) 1.0f else 0.5f)),
            contentDescription = "Arrow Next",
            modifier = Modifier
                .align(CenterVertically)
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    if (periodInfo.canGoForward) white else white.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable {
                    handleEvent(SleepDetailEvent.NextClicked)
                },
        )
    }
}

@Composable
fun SleepPhaseDescriptionCell() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp)
    ) {
        HtmlText(
            text = stringResource(id = R.string.deep_sleep_desc),
            style = MaterialTheme.typography.body1.copy(color = gray),
        )
        HtmlText(
            modifier = Modifier.padding(top = 24.dp),
            text = stringResource(id = R.string.rem_sleep_desc),
            style = MaterialTheme.typography.body1.copy(color = gray),
        )
        HtmlText(
            modifier = Modifier.padding(top = 24.dp),
            text = stringResource(id = R.string.light_sleep_desc),
            style = MaterialTheme.typography.body1.copy(color = gray),
        )
    }
}

@Composable
fun SleepQualityInfoCell(info: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp)
    ) {
        Image(
            modifier = Modifier.padding(top = 4.dp),
            painter = painterResource(id = R.drawable.ic_info),
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = info,
            style = MaterialTheme.typography.paragraph.copy(color = gray),
            textAlign = TextAlign.Justify,
        )
    }
}

@Preview
@Composable
fun SleepDetailsPreview() {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = typography,
    ) {
        Box(modifier = Modifier.background(background)) {
            SleepDetailsScreen(
                handleEvent = {},
                selectedRange = HistoryRange.Month,
                periodInfo = createMockPeriodInfo(),
                dailyInformation = createMockDailySleepDetails(),
                weeklyInformation = createMockWeeklySleepDetails(),
                monthlyInformation = createMockMonthlySleepDetails(),
                sleepDesc = "Your sleep average shows that you missed your 8 hour goal this week. We advise you to go to bed earlier",
                patients = emptyList(),
                selectedPatient = null,
            )
        }
    }
}