package com.athelohealth.mobile.presentation.ui.caregiver.selectPatient

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.model.patients.Patient
import com.athelohealth.mobile.presentation.ui.base.CircleAvatarImage
import com.athelohealth.mobile.presentation.ui.base.MainButton
import com.athelohealth.mobile.presentation.ui.base.SecondaryButton
import com.athelohealth.mobile.presentation.ui.theme.*


@Composable
fun SelectPatientCell(patient: Patient, onCellClick: (Patient) -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable { onCellClick(patient) }) {
        Text(
            text = stringResource(id = R.string.Select_ward),
            style = MaterialTheme.typography.body1.copy(color = gray)
        )
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            CircleAvatarImage(
                avatar = patient.image?.image5050,
                displayName = patient.name,
                modifier = Modifier.size(36.dp)
            )
            Text(
                modifier = Modifier.padding(8.dp),
                text = patient.name,
                style = MaterialTheme.typography.headline20.copy(color = black),
            )
            Image(painter = painterResource(id = R.drawable.arrows), contentDescription = null)
        }
    }
}

@Composable
fun PatientCell(patient: Patient, selected: Boolean, onCellClick: (Patient) -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable { onCellClick(patient) }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            CircleAvatarImage(
                avatar = patient.image?.image5050,
                displayName = patient.name,
                modifier = Modifier.size(36.dp)
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                text = patient.name,
                style = MaterialTheme.typography.subHeading.copy(color = gray),
            )
            Image(
                painter = painterResource(id = if (selected) R.drawable.ic_radio_on else R.drawable.ic_radio_off),
                contentDescription = null
            )
        }
        Divider(color = lightGray, thickness = 1.dp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoosePatientPopup(
    patients: List<Patient>,
    selectedPatient: Patient,
    onCancelClick: () -> Unit,
    onSwitchClick: (Patient) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(black.copy(0.2f))
    ) {
        Box {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(all = 16.dp),
                colors = CardDefaults.cardColors(containerColor = white),
                shape = RoundedCornerShape(size = 30.dp),
            ) {
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .padding(top = 16.dp),
                    text = stringResource(id = R.string.Select_your_ward),
                    style = MaterialTheme.typography.headline20.copy(color = darkPurple)
                )
                var tmpSelectedPatient by remember { mutableStateOf(selectedPatient) }
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(patients, key = { patient -> patient.userId }) { patient ->
                        PatientCell(
                            patient = patient,
                            selected = tmpSelectedPatient.userId == patient.userId,
                            onCellClick = { tmpSelectedPatient = it }
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    SecondaryButton(
                        modifier = Modifier
                            .padding(horizontal = 4.dp, vertical = 16.dp)
                            .width(120.dp),
                        text = R.string.Cancel,
                        onClick = { onCancelClick() },
                        textPadding = PaddingValues(0.dp),
                        background = white,
                        border = darkPurple,
                        textColor = darkPurple,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    MainButton(
                        modifier = Modifier
                            .padding(horizontal = 4.dp, vertical = 16.dp)
                            .widthIn(min = 120.dp),
                        textId = R.string.Switch,
                        onClick = { onSwitchClick(tmpSelectedPatient) })
                }
            }
        }
    }
}

@Preview
@Composable
fun SelectPatientPrev() {
    AtheloTheme {
        Column {
            SelectPatientCell(patient = Patient("1", "Test User", "Friend", null), onCellClick = {})
            PatientCell(
                patient = Patient("1", "Test User2", "Friend", null),
                onCellClick = {},
                selected = true
            )
            PatientCell(
                patient = Patient("1", "Test User3", "Friend", null),
                onCellClick = {},
                selected = false
            )

            val items = listOf(
                Patient("1", "Test User1", "Friend", null),
                Patient("2", "Test User2", "Friend", null),
                Patient("3", "Test User3", "Friend", null),
                Patient("4", "Test User4", "Friend", null),
                Patient("5", "Test User5", "Friend", null),
                Patient("6", "Test User6", "Friend", null),
            )
            ChoosePatientPopup(
                patients = items,
                selectedPatient = items.first(),
                onCancelClick = {},
                onSwitchClick = {})
        }
    }
}