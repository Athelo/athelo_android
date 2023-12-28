@file:OptIn(ExperimentalMaterial3Api::class)

package com.athelohealth.mobile.presentation.ui.caregiver.myWard

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.model.patients.Patient
import com.athelohealth.mobile.presentation.ui.base.*
import com.athelohealth.mobile.presentation.ui.theme.*

@Composable
fun MyWardScreen(viewModel: MyWardViewModel) {
    val state by viewModel.state.collectAsState()
    BoxScreen(viewModel = viewModel, showProgressProvider = { state.isLoading }) {
        Content(handleEvent = viewModel::handleEvent, patients = state.patients)
        PatientOptionPopup(
            handleEvent = viewModel::handleEvent,
            patient = state.showPatientMoreOption
        )
        val deletePatient = state.showPatientDeleteConfirmation
        if (deletePatient != null) {
            ConfirmDeletePopup(viewModel::handleEvent, deletePatient, state.patients.size <= 1)
        }
    }
}

@Composable
private fun Content(handleEvent: (MyWardEvent) -> Unit, patients: List<Patient>) {
    Column {
        ScreenNameToolbar(
            leftButton = {
                BackButton {
                    handleEvent(MyWardEvent.BackButtonClick)
                }
            },
            screenName = stringResource(id = R.string.My_Wards),
            rightButton = {
                Image(
                    painter = painterResource(id = R.drawable.plus_small),
                    contentDescription = "Add Ward",
                    modifier = Modifier
                        .size(56.dp)
                        .clickable(onClick = { handleEvent(MyWardEvent.AddPatientButtonClick) }),
                    contentScale = ContentScale.Inside
                )
            },
        )
        LazyColumn(contentPadding = PaddingValues(top = 24.dp, bottom = 53.dp)) {
            items(items = patients, key = { patient -> patient.userId }) { patient ->
                WardCell(handleEvent = handleEvent, patient = patient)
            }
        }
    }
}

@Composable
private fun WardCell(
    handleEvent: (MyWardEvent) -> Unit,
    patient: Patient,
    bgShape: Shape = RoundedCornerShape(30.dp)
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = bgShape,
        colors = CardDefaults.cardColors(containerColor = white),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .clip(bgShape)
                /*.clickable {
                    handleEvent(MyWardEvent.PatientClick(patient))
                }*/
                .padding(16.dp)
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            RoundedCornerAvatarImage(
                avatar = patient.image?.image100100,
                displayName = patient.name,
                shape = RoundedCornerShape(20.dp)
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 22.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = patient.name,
                    style = MaterialTheme.typography.subHeading.copy(color = gray)
                )
                Text(
                    text = patient.relation,
                    style = MaterialTheme.typography.body1.copy(color = gray.copy(alpha = 0.8f))
                )
            }
            Image(
                painter = painterResource(id = R.drawable.ic_more),
                contentDescription = null,
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { handleEvent(MyWardEvent.ShowMoreOptionClick(patient)) })
        }
    }
}

@Composable
fun PatientOptionPopup(handleEvent: (MyWardEvent) -> Unit, patient: Patient?) {
    AnimatedVisibility(visible = patient != null, enter = fadeIn(), exit = fadeOut()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(black.copy(alpha = 0.2f))
                .clickable(enabled = false) {

                },
            contentAlignment = Alignment.BottomCenter,
        ) {
            val targetH = androidx.compose.ui.platform.LocalConfiguration.current.screenHeightDp
            AnimatedVisibility(
                visible = patient != null,
                enter = slideInVertically(
                    initialOffsetY = { targetH },
                    animationSpec = tween(delayMillis = 300, durationMillis = 800)
                ),
                exit = slideOutVertically(targetOffsetY = { targetH })
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                ) {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        colors = CardDefaults.cardColors(background),
                        shape = RoundedCornerShape(30.dp)
                    ) {
                        OptionButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (patient != null)
                                        handleEvent(MyWardEvent.SendMessageClick(patient))
                                },
                            iconRes = R.drawable.ic_message,
                            textRes = R.string.Send_a_message,
                            paddingValues = PaddingValues(
                                start = 24.dp,
                                top = 24.dp,
                                end = 24.dp,
                                bottom = 12.dp
                            )
                        )
                        OptionButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (patient != null)
                                        handleEvent(MyWardEvent.DeleteWardClick(patient))
                                },
                            iconRes = R.drawable.ic_trash_bin,
                            textRes = R.string.Delete_ward,
                            color = red,
                            paddingValues = PaddingValues(
                                start = 24.dp,
                                top = 12.dp,
                                end = 24.dp,
                                bottom = 24.dp
                            )
                        )
                    }

                    SecondaryButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp),
                        text = R.string.Cancel,
                        onClick = { if (patient != null) handleEvent(MyWardEvent.CancelClick) },
                        background = background,
                        border = darkPurple,
                        textColor = darkPurple
                    )
                }
            }
        }
    }
}

@Composable
fun OptionButton(
    modifier: Modifier,
    iconRes: Int,
    textRes: Int,
    color: Color = darkPurple,
    paddingValues: PaddingValues
) =
    Row(modifier) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = paddingValues.calculateStartPadding(layoutDirection = LayoutDirection.Ltr),
                    bottom = paddingValues.calculateBottomPadding(),
                    end = 16.dp,
                )
                .size(24.dp),
            colorFilter = ColorFilter.tint(color, BlendMode.SrcAtop)
        )
        Text(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = paddingValues.calculateBottomPadding(),
                )
                .weight(1f),
            text = stringResource(id = textRes),
            style = MaterialTheme.typography.button.copy(color),
        )
    }

@Composable
private fun ConfirmDeletePopup(
    handleEvent: (MyWardEvent) -> Unit,
    patient: Patient,
    isLastPatient: Boolean
) {
    DeletePopup(
        onConfirmClick = { handleEvent(MyWardEvent.DeleteWardConfirmationClick(patient)) },
        onCancelClick = { handleEvent(MyWardEvent.CancelClick) },
        title = stringResource(id = R.string.Delete_a_Ward),
        description = stringResource(
            id = if (isLastPatient) R.string.Confirm_Delete_Ward_Last_message else R.string.confirmation_delete_ward,
            patient.name,
        ),
    )
}