@file:OptIn(ExperimentalMaterial3Api::class)

package com.i2asolutions.athelo.presentation.ui.share.sleepSummary

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.model.member.User
import com.i2asolutions.athelo.presentation.model.patients.Patient
import com.i2asolutions.athelo.presentation.model.sleep.SleepSummaryScreen.*
import com.i2asolutions.athelo.presentation.ui.base.BoxScreen
import com.i2asolutions.athelo.presentation.ui.base.MainButton
import com.i2asolutions.athelo.presentation.ui.base.ToolbarWithMenuAndMyProfile
import com.i2asolutions.athelo.presentation.ui.base.chart.CircleChart
import com.i2asolutions.athelo.presentation.ui.caregiver.selectPatient.ChoosePatientPopup
import com.i2asolutions.athelo.presentation.ui.caregiver.selectPatient.SelectPatientCell
import com.i2asolutions.athelo.presentation.ui.theme.*
import com.i2asolutions.athelo.utils.createMockIdealSleep
import com.i2asolutions.athelo.utils.createMockSleepInformation
import com.i2asolutions.athelo.utils.createMockSleepResult
import com.i2asolutions.athelo.utils.createMockUser

@Composable
fun SleepSummaryScreen(viewModel: SleepSummaryViewModel) {
    val state by viewModel.viewState.collectAsState()
    BoxScreen(
        modifier = Modifier,
        viewModel = viewModel,
        showProgressProvider = { state.isLoading },
        includeStatusBarPadding = false
    ) {
        if (state.showNotConnected) {
            SleepSummaryNotConnectedScreen(
                viewModel::handleEvent,
                currentUser = state.currentUser
            )
        } else {
            SleepSummaryScreen(
                handleEvent = viewModel::handleEvent,
                currentUser = state.currentUser,
                idealSleep = state.idealSleep,
                sleepResult = state.sleepResult,
                sleepInformation = state.sleepInformation,
                selectedPatient = state.selectedPatient,
                patients = state.patients
            )
        }
    }
}

@Composable
fun SleepSummaryScreen(
    handleEvent: (SleepSummaryEvent) -> Unit,
    currentUser: User,
    idealSleep: IdealSleep?,
    sleepResult: SleepResult?,
    sleepInformation: SleepInformation?,
    selectedPatient: Patient?,
    patients: List<Patient>,
) {
    var showPatientChooser by remember {
        mutableStateOf(false)
    }
    Column {
        ToolbarWithMenuAndMyProfile(
            userAvatar = currentUser.photo?.image5050,
            userDisplayName = currentUser.displayName ?: "",
            menuClick = {
                handleEvent(SleepSummaryEvent.MenuClick)
            },
            avatarClick = {
                handleEvent(SleepSummaryEvent.MyProfileClick)
            }
        )
        if (selectedPatient != null)
            SelectPatientCell(patient = selectedPatient, onCellClick = {
                showPatientChooser = true
            })
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = false),
            onRefresh = { handleEvent(SleepSummaryEvent.RefreshData) }) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(bottom = 56.dp, top = 8.dp)
            ) {
                if (idealSleep != null) item {
                    IdealSleepCell(idealSleep, handleEvent)
                }
                if (sleepResult != null) item {
                    SleepResultCell(sleepResult)
                }
                if (sleepInformation != null) item {
                    SleepInformationCell(sleepInformation, handleEvent)
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
                handleEvent(SleepSummaryEvent.ChangePatient(patient))
                showPatientChooser = false
            })
    }
}

@Composable
fun SleepSummaryNotConnectedScreen(
    handleEvent: (SleepSummaryEvent) -> Unit,
    currentUser: User,
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp),
    ) {

        Column {
            ToolbarWithMenuAndMyProfile(
                modifier = Modifier.drawBehind {
                    drawRect(greenStatusBar)
                },
                userAvatar = currentUser.photo?.image5050,
                userDisplayName = currentUser.displayName ?: "",
                menuClick = {
                    handleEvent(SleepSummaryEvent.MenuClick)
                },
                avatarClick = {
                    handleEvent(SleepSummaryEvent.MyProfileClick)
                }
            )
            Image(
                modifier = Modifier
                    .fillMaxWidth(),
                painter = painterResource(id = R.drawable.sleep_not_connected),
                contentDescription = stringResource(id = R.string.app_name),
                contentScale = ContentScale.FillWidth
            )
        }
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp, bottom = 8.dp)
                .fillMaxWidth(),
            text = stringResource(id = R.string.This_page_is_currently_empty),
            style = MaterialTheme.typography.headline20.copy(
                fontSize = 20.sp,
                color = black,
                textAlign = TextAlign.Center
            ),
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
                .fillMaxWidth(),
            text = stringResource(id = R.string.This_page_is_currently_empty_desc),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 18.sp,
                color = gray,
                textAlign = TextAlign.Center
            )
        )
        MainButton(
            textId = R.string.Connect_SmartWatch,
            background = purple,
            onClick = {
                handleEvent(SleepSummaryEvent.ConnectSmartWatchClick)
            },
        )
    }
}

@Composable
fun IdealSleepCell(idealSleep: IdealSleep, handleEvent: (SleepSummaryEvent) -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .padding(bottom = 24.dp)
            .shadow(shape = RoundedCornerShape(30.dp), elevation = 5.dp),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = white
        )
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = idealSleep.title,
                    style = MaterialTheme.typography.headline20.copy(
                        color = gray,
                        textAlign = TextAlign.Left
                    ),
                    modifier = Modifier.padding(start = 16.dp, top = 24.dp)
                )
                Text(
                    text = idealSleep.time,
                    style = MaterialTheme.typography.headline24.copy(
                        color = lightOlivaceous,
                        textAlign = TextAlign.Left
                    ),
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
                Text(
                    text = stringResource(id = R.string.Read_Article),
                    style = MaterialTheme.typography.textField.copy(
                        color = lightOlivaceous,
                        textAlign = TextAlign.Left,
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier
                        .padding(start = 16.dp, top = 4.dp, bottom = 16.dp)
                        .clickable {
                            handleEvent(SleepSummaryEvent.ReadArticleClick(idealSleep.articleId))
                        }
                )
            }
            Image(
                modifier = Modifier.padding(end = 16.dp, top = 24.dp),
                painter = painterResource(id = idealSleep.image),
                contentDescription = stringResource(id = R.string.app_name)
            )
        }
    }
}

@Composable
fun SleepResultCell(sleepResult: SleepResult) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .padding(bottom = 24.dp)
            .shadow(shape = RoundedCornerShape(30.dp), elevation = 5.dp),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = white
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(.4f),
                painter = painterResource(id = R.drawable.ic_bg_sleep),
                contentDescription = "",
                contentScale = ContentScale.Crop,
            )
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = CenterVertically
            ) {
                Text(
                    text = sleepResult.text,
                    style = MaterialTheme.typography.paragraph.copy(
                        color = gray,
                        textAlign = TextAlign.Left
                    ),
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f)
                        .align(CenterVertically)
                )
                CircleChart(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .size(100.dp)
                        .align(CenterVertically),
                    chartData = sleepResult.chartDataSet
                )
            }
        }
    }
}

@Composable
fun SleepInformationCell(
    sleepInformation: SleepInformation,
    handleEvent: (SleepSummaryEvent) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .padding(bottom = 24.dp)
            .shadow(shape = RoundedCornerShape(30.dp), elevation = 5.dp),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = white
        )
    ) {
        Text(
            text = stringResource(id = R.string.Sleep_information),
            style = MaterialTheme.typography.headline20.copy(
                color = gray,
                textAlign = TextAlign.Left
            ),
            modifier = Modifier.padding(start = 24.dp, top = 16.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                SleepSummaryStageCell(
                    sleepInformation.deepSleep,
                    R.drawable.sleep_icon_deep,
                    R.string.Deep_Sleep,
                    24.dp
                )
                SleepSummaryStageCell(
                    sleepInformation.lightSleep,
                    R.drawable.sleep_icon_light,
                    R.string.Light_Sleep,
                    16.dp
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                SleepSummaryStageCell(
                    sleepInformation.rem,
                    R.drawable.sleep_icon_rem,
                    R.string.REM_Sleep,
                    24.dp
                )
                SleepSummaryStageCell(
                    sleepInformation.awake,
                    R.drawable.sleep_icon_awake,
                    R.string.Awake,
                    16.dp
                )
            }
        }
        Text(
            text = stringResource(id = R.string.See_More),
            style = MaterialTheme.typography.textField.copy(
                color = lightOlivaceous,
                textAlign = TextAlign.Left,
                textDecoration = TextDecoration.Underline
            ),
            modifier = Modifier
                .padding(start = 24.dp, top = 24.dp, bottom = 16.dp)
                .clickable {
                    handleEvent(SleepSummaryEvent.MoreClick)
                }
        )
    }
}

@Composable
fun SleepSummaryStageCell(
    value: String,
    @DrawableRes stageIcon: Int,
    @StringRes stageName: Int,
    topPadding: Dp
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = topPadding)
    ) {
        Image(
            painter = painterResource(id = stageIcon),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelLarge.copy(
                color = black,
            ),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
    Text(
        text = stringResource(id = stageName),
        style = MaterialTheme.typography.bodyMedium.copy(
            color = gray,
        ),
        modifier = Modifier.padding(start = 32.dp)
    )
}

@Preview
@Composable
fun ChatSummaryPreview() {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = typography,
    ) {
        Box(modifier = Modifier.background(background)) {
            SleepSummaryScreen(
                handleEvent = {},
                currentUser = createMockUser(1),
                idealSleep = createMockIdealSleep(),
                sleepResult = createMockSleepResult(),
                sleepInformation = createMockSleepInformation(),
                selectedPatient = null,
                patients = emptyList()
            )
        }
    }
}