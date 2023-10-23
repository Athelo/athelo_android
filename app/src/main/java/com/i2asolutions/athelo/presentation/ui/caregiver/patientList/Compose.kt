@file:OptIn(ExperimentalMaterial3Api::class)

package com.i2asolutions.athelo.presentation.ui.caregiver.patientList

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.model.patients.Patient
import com.i2asolutions.athelo.presentation.ui.base.BoxScreen
import com.i2asolutions.athelo.presentation.ui.base.MainButton
import com.i2asolutions.athelo.presentation.ui.base.SecondaryWithImageButton
import com.i2asolutions.athelo.presentation.ui.base.ToolbarWithNameBack
import com.i2asolutions.athelo.presentation.ui.theme.*

@Composable
fun PatientListScreen(viewModel: PatientListViewModel) {
    val state by viewModel.state.collectAsState()
    BoxScreen(viewModel = viewModel, showProgressProvider = { state.isLoading }) {
        if (!state.isLoading) {
            if (state.patientList.isEmpty()) {
                EmptyPatientContent(handleEvent = viewModel::handleEvent)
            } else {
                PatientListContent(
                    handleEvent = viewModel::handleEvent,
                    patients = state.patientList,
                    selectedPatientProvider = { state.selectedPatient },
                    enableButtonProvider = { state.enableProceedButton }
                )
            }
        }
    }
}

@Composable
private fun EmptyPatientContent(handleEvent: (PatientListEvent) -> Unit) {
    Column {
        Box {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = R.drawable.ic_caregiver_empty),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
            ToolbarWithNameBack(
                backClick = { handleEvent(PatientListEvent.BackButtonClick) },
                screenName = stringResource(id = R.string.Act_as_Caregiver)
            )
        }
        Text(
            text = stringResource(id = R.string.This_page_is_currently_empty),
            style = MaterialTheme.typography.headline20.copy(
                color = black, textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
        )
        Text(
            text = stringResource(id = R.string.This_page_is_currently_empty_desc_patients),
            style = MaterialTheme.typography.normalText.copy(
                color = purple, textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        SecondaryWithImageButton(
            modifier = Modifier
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                .navigationBarsPadding(),
            textRes = R.string.Add_a_Ward,
            onClick = { handleEvent(PatientListEvent.AddPatientClick) },
            imageRes = R.drawable.ic_ward,
            background = background,
            border = purple,
            textColor = purple
        )
    }
}

@Composable
private fun PatientListContent(
    handleEvent: (PatientListEvent) -> Unit,
    patients: List<Patient>,
    selectedPatientProvider: () -> Patient? = { null },
    enableButtonProvider: () -> Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        ToolbarWithNameBack(
            backClick = { handleEvent(PatientListEvent.BackButtonClick) },
            screenName = stringResource(id = R.string.Act_as_Caregiver)
        )
        Text(
            text = stringResource(id = R.string.act_as_caregiver_message),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            style = MaterialTheme.typography.normalText.copy(color = darkPurple)
        )
        LazyColumn(modifier = Modifier.weight(1f), contentPadding = PaddingValues(bottom = 56.dp)) {
            items(items = patients, key = { it.userId }) { patient ->
                PatientCell(
                    handleEvent = handleEvent,
                    patient = patient,
                    selectedProvider = { selectedPatientProvider()?.userId == patient.userId },
                )
            }
        }
        MainButton(
            textId = R.string.Proceed,
            enableButton = enableButtonProvider(),
            onClick = {
                handleEvent(PatientListEvent.ProceedClick)
            },
        )
        Spacer(modifier = Modifier.height(16.dp))
        SecondaryWithImageButton(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .padding(4.dp),
            textRes = R.string.Add_a_Ward,
            imageRes = R.drawable.ic_ward,
            onClick = { handleEvent(PatientListEvent.AddPatientClick) },
            background = background,
            border = purple,
            textColor = purple,
            fillTextFullWidth = false,
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Suppress("UNUSED_PARAMETER")
@Composable
private fun PatientCell(
    handleEvent: (PatientListEvent) -> Unit,
    patient: Patient,
    selectedProvider: () -> Boolean = { false },
    cardBg: Shape = RoundedCornerShape(20.dp),
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .fillMaxWidth(),
        shape = cardBg,
        colors = CardDefaults.cardColors(containerColor = white),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .clip(cardBg)
                .clickable { handleEvent(PatientListEvent.PatientCellClick(patient)) }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Image(
                painter = painterResource(id = R.drawable.caregiver),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = stringResource(id = R.string.act_as_Caregiver_for, patient.name),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                style = MaterialTheme.typography.body1.copy(color = gray)
            )
            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = if (selectedProvider()) R.drawable.ic_radio_on else R.drawable.ic_radio_off),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun EmptyPrev() {
    AtheloTheme {
        EmptyPatientContent(handleEvent = {})

    }
}