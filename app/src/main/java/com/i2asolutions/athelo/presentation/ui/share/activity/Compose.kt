@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.i2asolutions.athelo.presentation.ui.share.activity

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.model.activity.ActivityScreen
import com.i2asolutions.athelo.presentation.model.member.User
import com.i2asolutions.athelo.presentation.model.patients.Patient
import com.i2asolutions.athelo.presentation.ui.base.BoxScreen
import com.i2asolutions.athelo.presentation.ui.base.MainButton
import com.i2asolutions.athelo.presentation.ui.base.ToolbarWithMenuAndMyProfile
import com.i2asolutions.athelo.presentation.ui.base.chart.SimpleBarChart
import com.i2asolutions.athelo.presentation.ui.base.chart.SimpleLineChart
import com.i2asolutions.athelo.presentation.ui.caregiver.selectPatient.ChoosePatientPopup
import com.i2asolutions.athelo.presentation.ui.caregiver.selectPatient.SelectPatientCell
import com.i2asolutions.athelo.presentation.ui.theme.*
import com.i2asolutions.athelo.utils.*

@Composable
fun ActivityScreen(viewModel: ActivityViewModel) {
    val state by viewModel.viewState.collectAsState()
    BoxScreen(
        modifier = Modifier,
        viewModel = viewModel,
        showProgressProvider = { state.isLoading },
        includeStatusBarPadding = false
    ) {
        println(state.selectedPatient)
        if (state.showNotConnected) {
            ActivityNotConnectedScreen(
                viewModel::handleEvent,
                currentUser = state.currentUser
            )
        } else {
            ActivityScreen(
                handleEvent = viewModel::handleEvent,
                currentUser = state.currentUser,
                topHint = state.topHint,
                steps = state.stepsInformation,
                activity = state.activityInformation,
                heartRate = state.heartRateInformation,
                hrv = state.hrvInformation,
                selectedPatient = state.selectedPatient,
                patients = state.patients,
            )
        }
    }
}

@Composable
fun ActivityScreen(
    handleEvent: (ActivityEvent) -> Unit,
    currentUser: User,
    topHint: String,
    steps: ActivityScreen.Steps?,
    activity: ActivityScreen.Activity?,
    heartRate: ActivityScreen.HeartRate?,
    hrv: ActivityScreen.HeartRateVariability?,
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
                handleEvent(ActivityEvent.MenuClick)
            },
            avatarClick = {
                handleEvent(ActivityEvent.MyProfileClick)
            }
        )
        if (selectedPatient != null)
            SelectPatientCell(patient = selectedPatient, onCellClick = {
                showPatientChooser = true
            })
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = false),
            onRefresh = { handleEvent(ActivityEvent.RefreshData) }) {
            LazyVerticalGrid(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
                contentPadding = PaddingValues(bottom = 56.dp, top = 8.dp),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
            ) {

                if (topHint.isNotBlank()) item(span = { GridItemSpan(2) }) {
                    ActivityTopHintCell(topHint)
                }
                if (steps != null) item {
                    StepsCell(steps, handleEvent)
                }
                if (activity != null) item {
                    ActivityCell(activity, handleEvent)
                }
                if (heartRate != null) item {
                    HeartRateCell(heartRate, handleEvent)
                }
                if (hrv != null) item {
                    HeartRateVariabilityCell(hrv, handleEvent)
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
                handleEvent(ActivityEvent.ChangePatient(patient))
                showPatientChooser = false
            })
    }
}

@Composable
fun ActivityNotConnectedScreen(
    handleEvent: (ActivityEvent) -> Unit,
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
                    handleEvent(ActivityEvent.MenuClick)
                },
                avatarClick = {
                    handleEvent(ActivityEvent.MyProfileClick)
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
                handleEvent(ActivityEvent.ConnectSmartWatchClick)
            },
        )
    }
}

@Composable
fun ActivityTopHintCell(hint: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
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
            Text(
                text = hint,
                style = MaterialTheme.typography.paragraph.copy(
                    color = gray,
                    textAlign = TextAlign.Left
                ),
                modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxWidth()
                    .align(Center)
            )
        }
    }
}

@Composable
fun StepsCell(data: ActivityScreen.Steps, handleEvent: (ActivityEvent) -> Unit) {
    BarChartCell(
        icon = R.drawable.ic_person2,
        text = R.string.Steps,
        visibleValue = data.value,
        data = data.data,
        onClickAction = {
            handleEvent(ActivityEvent.StepsClick)
        }
    )
}

@Composable
fun ActivityCell(data: ActivityScreen.Activity, handleEvent: (ActivityEvent) -> Unit) {
    BarChartCell(
        icon = R.drawable.ic_fi_sr_gym,
        text = R.string.Activity,
        visibleValue = data.value,
        data = data.data,
        onClickAction = {
            handleEvent(ActivityEvent.ActivityClick)
        }
    )
}

@Composable
fun HeartRateCell(data: ActivityScreen.HeartRate, handleEvent: (ActivityEvent) -> Unit) {
    BarChartCell(
        icon = R.drawable.ic_fi_sr_heart,
        text = R.string.Heart_rate,
        visibleValue = data.value,
        data = data.data,
        onClickAction = {
            handleEvent(ActivityEvent.HeartRateClick)
        }
    )
}

@Composable
fun HeartRateVariabilityCell(
    data: ActivityScreen.HeartRateVariability,
    handleEvent: (ActivityEvent) -> Unit
) {
    LineChartCell(
        icon = R.drawable.ic_fi_sr_heart,
        text = R.string.Heart_rate_variability,
        data = data.data,
        onClickAction = {
            handleEvent(ActivityEvent.HrvClick)
        }
    )
}

@Composable
fun BarChartCell(
    @DrawableRes icon: Int,
    @StringRes text: Int,
    visibleValue: String,
    data: List<Float>,
    onClickAction: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(shape = RoundedCornerShape(20.dp), elevation = 5.dp)
            .clickable { onClickAction() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = white
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(
                    modifier = Modifier
                        .size(28.dp)
                        .align(CenterVertically),
                    painter = painterResource(id = icon),
                    contentDescription = "",
                )
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp)
                ) {
                    Text(
                        text = stringResource(id = text),
                        style = MaterialTheme.typography.subtitle.copy(
                            color = lightOlivaceous,
                            textAlign = TextAlign.Left
                        ),
                    )
                    Text(
                        text = visibleValue,
                        style = MaterialTheme.typography.subtitle.copy(
                            color = lightOlivaceous,
                            textAlign = TextAlign.Left
                        ),
                        modifier = Modifier
                            .alpha(0.5f)
                    )
                }
            }
            SimpleBarChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .weight(1f),
                data = data
            )
        }
    }
}

@Composable
fun LineChartCell(
    @DrawableRes icon: Int,
    @StringRes text: Int,
    data: List<Float>,
    onClickAction: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(shape = RoundedCornerShape(20.dp), elevation = 5.dp)
            .clickable { onClickAction() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = white
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(
                    modifier = Modifier
                        .size(28.dp),
                    painter = painterResource(id = icon),
                    contentDescription = "",
                )
                Box(
                    modifier = Modifier
                        .padding(start = 16.dp)
                ) {
                    Text(
                        text = stringResource(id = text),
                        style = MaterialTheme.typography.subtitle.copy(
                            color = lightOlivaceous,
                            textAlign = TextAlign.Left
                        ),
                        modifier = Modifier.align(TopStart)
                    )
                }
            }
            SimpleLineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .weight(1f),
                data = data
            )
        }
    }
}


@Preview
@Composable
fun ActivityPreview() {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = typography,
    ) {
        Box(modifier = Modifier.background(background)) {
            ActivityScreen(
                handleEvent = {},
                currentUser = createMockUser(0),
                topHint = "You walked less yesterday than you did the day before",
                steps = createMockStepsInformation(),
                activity = createMockActivityInformation(),
                heartRate = createMockHRInformation(),
                hrv = createMockHRVInformation(),
                selectedPatient = null,
                patients = emptyList(),
            )
        }
    }
}