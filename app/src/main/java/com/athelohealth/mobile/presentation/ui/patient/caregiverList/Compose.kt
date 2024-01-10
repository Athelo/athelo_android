@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.athelohealth.mobile.presentation.ui.patient.caregiverList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.model.caregiver.Caregiver
import com.athelohealth.mobile.presentation.ui.base.*
import com.athelohealth.mobile.presentation.ui.theme.*

@Composable
fun CaregiverListScreen(viewModel: CaregiverListViewModel) {
    val viewState by viewModel.viewState.collectAsState()
    BoxScreen(viewModel = viewModel, showProgressProvider = { viewState.isLoading }) {
        if (viewState.myCaregivers.isEmpty()) {
            EmptyCaregiverScreen(handleEvent = viewModel::handleEvent)
        } else {
            MyCaregiversScreen(
                handleEvent = viewModel::handleEvent,
                caregivers = viewState.myCaregivers
            )
            val deletePatient = viewState.showCaregiverDeleteConfirmation
            if (deletePatient != null) {
                ConfirmDeletePopup(viewModel::handleEvent, deletePatient)
            }
        }
    }
}

@Composable
private fun EmptyCaregiverScreen(handleEvent: (CaregiverListEvent) -> Unit) {
    Column {
        Box {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = R.drawable.my_caregiver_empty),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
            ToolbarWithNameBack(
                backClick = { handleEvent(CaregiverListEvent.BackButtonClick) },
                screenName = stringResource(id = R.string.Act_as_Patient)
            )
        }
        Text(
            text = stringResource(id = R.string.This_page_is_currently_empty),
            style = MaterialTheme.typography.headline20.copy(
                color = black,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
        )
        Text(
            text = stringResource(id = R.string.This_page_is_currently_empty_desc_caregivers),
            style = MaterialTheme.typography.normalText.copy(
                color = purple,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        MainButton(
            textId = R.string.Proceed,
            onClick = {
                handleEvent(CaregiverListEvent.ProceedClick)
            },
        )
        Spacer(modifier = Modifier.height(16.dp))
        SecondaryWithImageButton(
            modifier = Modifier
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                .navigationBarsPadding(),
            textRes = R.string.Add_a_caregiver,
            onClick = { handleEvent(CaregiverListEvent.AddCaregiverClick) },
            imageRes = R.drawable.ic_add_caregiver,
            background = background,
            border = purple,
            textColor = purple
        )
    }
}

@Composable
private fun MyCaregiversScreen(
    handleEvent: (CaregiverListEvent) -> Unit,
    caregivers: List<Caregiver>
) {
    Column(modifier = Modifier.navigationBarsPadding()) {
        ToolbarWithNameBack(
            backClick = { handleEvent(CaregiverListEvent.BackButtonClick) },
            screenName = stringResource(id = R.string.Act_as_Patient)
        )
        Text(
            text = stringResource(id = R.string.This_page_is_currently_empty_desc_caregivers),
            style = MaterialTheme.typography.normalText.copy(
                color = purple,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(items = caregivers, key = { it.id }) {
                CaregiverCell(handleEvent = handleEvent, caregiver = it)
            }
        }
        MainButton(
            textId = R.string.Proceed,
            onClick = {
                handleEvent(CaregiverListEvent.ProceedClick)
            },
        )
        Spacer(modifier = Modifier.height(16.dp))
        SecondaryWithImageButton(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .padding(4.dp),
            textRes = R.string.Add_a_caregiver,
            imageRes = R.drawable.ic_add_caregiver,
            fillTextFullWidth = false,
            onClick = { handleEvent(CaregiverListEvent.AddCaregiverClick) },
            background = background,
            border = purple
        )
        Spacer(modifier = Modifier.height(16.dp))

    }
}

@Composable
private fun CaregiverCell(
    handleEvent: (CaregiverListEvent) -> Unit,
    caregiver: Caregiver,
    cardBg: Shape = RoundedCornerShape(30.dp)
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
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RoundedCornerAvatarImage(
                avatar = caregiver.photo?.image5050,
                displayName = caregiver.displayName,
                shape = RoundedCornerShape(20.dp)
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 22.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = caregiver.displayName,
                    style = MaterialTheme.typography.subHeading.copy(color = gray)
                )
                Text(
                    text = caregiver.relation,
                    style = MaterialTheme.typography.body1.copy(color = gray.copy(alpha = 0.8f))
                )
            }
            Image(
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = null,
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { handleEvent(CaregiverListEvent.SelectCaregiverClick(caregiver)) })
        }
    }
}

@Composable
private fun ConfirmDeletePopup(
    handleEvent: (CaregiverListEvent) -> Unit,
    caregiver: Caregiver,
) {
    DeletePopup(
        onConfirmClick = { handleEvent(CaregiverListEvent.ConfirmationDeleteClick(caregiver)) },
        onCancelClick = { handleEvent(CaregiverListEvent.CancelClick) },
        title = stringResource(id = R.string.Delete_a_Caregiver),
        description = stringResource(id = R.string.Confirm_Delete_Caregiver_Last_message)
    )
}

// Previews
@Preview
@Composable
fun EmptyPreview() {
    AtheloTheme {
        Box(modifier = Modifier.background(background)) {
            EmptyCaregiverScreen(handleEvent = {})
        }
    }
}