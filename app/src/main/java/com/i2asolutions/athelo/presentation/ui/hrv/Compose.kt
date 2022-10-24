@file:OptIn(ExperimentalMaterial3Api::class)

package com.i2asolutions.athelo.presentation.ui.hrv

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.model.activity.ActivityGraphScreen
import com.i2asolutions.athelo.presentation.model.base.ChartCloudContent
import com.i2asolutions.athelo.presentation.model.base.InfoCell
import com.i2asolutions.athelo.presentation.model.base.PeriodInfoCell
import com.i2asolutions.athelo.presentation.model.sleep.HistoryRange
import com.i2asolutions.athelo.presentation.model.sleep.SleepDetailScreen
import com.i2asolutions.athelo.presentation.ui.base.BoxScreen
import com.i2asolutions.athelo.presentation.ui.base.RadioButton
import com.i2asolutions.athelo.presentation.ui.base.RadioButtonGroup
import com.i2asolutions.athelo.presentation.ui.base.ToolbarWithNameBack
import com.i2asolutions.athelo.presentation.ui.base.chart.LineChart
import com.i2asolutions.athelo.presentation.ui.theme.*

@Composable
fun HrvScreen(viewModel: HrvViewModel) {
    val state = viewModel.viewState.collectAsState()
    BoxScreen(
        viewModel = viewModel,
        showProgressProvider = { state.value.isLoading },
        includeStatusBarPadding = true
    ) {
        SleepDetailsScreen(
            handleEvent = viewModel::handleEvent,
            selectedRange = state.value.selectedRange,
            periodInfo = state.value.periodInfo,
            information = state.value.information,
            sleepDesc = state.value.desc,
        )
    }
}

@Composable
fun SleepDetailsScreen(
    handleEvent: (HrvEvent) -> Unit,
    selectedRange: HistoryRange = HistoryRange.Day,
    periodInfo: SleepDetailScreen.PeriodInfo?,
    information: ActivityGraphScreen.ActivityLineInformation?,
    sleepDesc: String? = null,
) {

    Column(
        modifier = Modifier
    ) {
        ToolbarWithNameBack(
            backClick = { handleEvent(HrvEvent.BackClick) },
            screenName = stringResource(id = R.string.Heart_Rate_Variability)
        )
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
                        handleEvent(HrvEvent.RangeChanged(HistoryRange.Day))
                    }
                ),
                RadioButton(
                    includePadding = false,
                    text = stringResource(id = R.string.Week),
                    onClick = {
                        handleEvent(HrvEvent.RangeChanged(HistoryRange.Week))
                    }),
                RadioButton(
                    includePadding = false,
                    text = stringResource(id = R.string.Month),
                    onClick = {
                        handleEvent(HrvEvent.RangeChanged(HistoryRange.Month))
                    })
            )
        )
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = false),
            onRefresh = { handleEvent(HrvEvent.RefreshData) }) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                if (periodInfo != null) item {
                    PeriodInfoCell(
                        periodInfo = periodInfo,
                        prevClick = { handleEvent(HrvEvent.PrevClicked) },
                        nextClick = { handleEvent(HrvEvent.NextClicked) }
                    )
                }
                if (information != null) item {
                    GraphCell(information, selectedRange)
                }
                if (sleepDesc != null && sleepDesc.isNotBlank()) item {
                    InfoCell(sleepDesc)
                }
                item {
                    Spacer(modifier = Modifier.navigationBarsPadding())
                }
            }
        }
    }
}

@Composable
fun GraphCell(info: ActivityGraphScreen.ActivityLineInformation, range: HistoryRange) {
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
                text = stringResource(id = range.overviewText),
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
        Text(
            modifier = Modifier.padding(top = 15.dp),
            text = stringResource(id = R.string.ms),
            style = MaterialTheme.typography.subtitle.copy(color = lightGray, fontSize = 10.sp)
        )
        LineChart(
            modifier = Modifier
                .height(330.dp),
            chartData = info.dataSet
        ) { entry ->
            ChartCloudContent(Modifier.align(Center), info.dataSet.cloudFormatter(entry))
        }
    }
}

@Preview
@Composable
fun SleepDetailsPreview() {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = typography,
    ) {
//        Box(modifier = Modifier.background(background)) {
//            SleepDetailsScreen(
//                handleEvent = {},
//                selectedRange = HistoryRange.Month,
//                periodInfo = createMockPeriodInfo(),
//                information = createMockStepsInformation(),
//                sleepDesc = "Your sleep average shows that you missed your 8 hour goal this week. We advise you to go to bed earlier",
//            )
//        }
    }
}