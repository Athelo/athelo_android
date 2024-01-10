package com.athelohealth.mobile.presentation.ui.share.steps

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.model.activity.ActivityGraphScreen
import com.athelohealth.mobile.presentation.model.base.ChartCloudContent
import com.athelohealth.mobile.presentation.model.base.InfoCell
import com.athelohealth.mobile.presentation.model.base.PeriodInfoCell
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
import com.athelohealth.mobile.presentation.ui.theme.*

@Composable
fun StepsScreen(viewModel: StepsViewModel) {
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
            information = viewState.information,
            desc = viewState.desc,
            selectedPatient = viewState.selectedPatient,
            patients = viewState.patients,
        )
    }
}

@Composable
fun SleepDetailsScreen(
    handleEvent: (StepsEvent) -> Unit,
    selectedRange: HistoryRange = HistoryRange.Day,
    periodInfo: SleepDetailScreen.PeriodInfo?,
    information: ActivityGraphScreen.ActivityInformation?,
    desc: String? = null,
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
            backClick = { handleEvent(StepsEvent.BackClick) },
            screenName = stringResource(id = R.string.Steps)
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
                        handleEvent(StepsEvent.RangeChanged(HistoryRange.Day))
                    }
                ),
                RadioButton(
                    includePadding = false,
                    text = stringResource(id = R.string.Week),
                    onClick = {
                        handleEvent(StepsEvent.RangeChanged(HistoryRange.Week))
                    }),
                RadioButton(
                    includePadding = false,
                    text = stringResource(id = R.string.Month),
                    onClick = {
                        handleEvent(StepsEvent.RangeChanged(HistoryRange.Month))
                    })
            )
        )
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = false),
            onRefresh = { handleEvent(StepsEvent.RefreshData) }) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                if (periodInfo != null) item {
                    PeriodInfoCell(
                        periodInfo = periodInfo,
                        prevClick = { handleEvent(StepsEvent.PrevClicked) },
                        nextClick = { handleEvent(StepsEvent.NextClicked) }
                    )
                }
                if (information != null) item {
                    GraphCell(information, selectedRange)
                }
                if (!desc.isNullOrBlank()) item {
                    InfoCell(desc)
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
                handleEvent(StepsEvent.ChangePatient(patient))
                showPatientChooser = false
            })
    }
}

@Composable
fun GraphCell(info: ActivityGraphScreen.ActivityInformation, range: HistoryRange) {
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
            modifier = Modifier.padding(top = 15.dp, bottom = 5.dp),
            text = stringResource(id = R.string.Steps),
            style = MaterialTheme.typography.subtitle.copy(color = lightGray, fontSize = 10.sp)
        )
        BarChart(
            modifier = Modifier
                .height(330.dp),
            chartData = info.dataSet,
            yAxisLabelWidth = 40.dp
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