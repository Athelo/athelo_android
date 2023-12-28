package com.athelohealth.mobile.presentation.ui.patient.myDevice

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.ui.base.BoxScreen
import com.athelohealth.mobile.presentation.ui.base.ConfirmPopup
import com.athelohealth.mobile.presentation.ui.base.MainButton
import com.athelohealth.mobile.presentation.ui.base.ToolbarWithNameBack
import com.athelohealth.mobile.presentation.ui.theme.*

@Composable
fun MyDeviceScreen(viewModel: MyDeviceViewModel) {
    val state by viewModel.state.collectAsState()
    BoxScreen(
        viewModel = viewModel,
        showProgressProvider = { state.isLoading },
        includeStatusBarPadding = false
    ) {
        Content(
            handleEvent = viewModel::handleEvent,
            showDisconnectConfirmation = { state.showDisconnectConfirmation },
            showForceDisconnectConfirmation = { state.showForceDisconnectConfirmation },
        )
    }
}

@Composable
private fun Content(
    handleEvent: (MyDeviceEvent) -> Unit,
    showDisconnectConfirmation: () -> Boolean,
    showForceDisconnectConfirmation: () -> Boolean
) {
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Box {
            Image(
                painter = painterResource(id = R.drawable.ic_my_device),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(id = R.string.Your_device_Fitbit_is_connected_to_the_app),
            style = MaterialTheme.typography.headline20.copy(
                color = black,
                textAlign = TextAlign.Center
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 16.dp)
                .fillMaxWidth()
        )
        Text(
            text = stringResource(id = R.string.Do_you_want_to_change_device),
            style = MaterialTheme.typography.paragraph.copy(
                color = gray,
                textAlign = TextAlign.Center
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 24.dp)
                .fillMaxWidth()
        )
        MainButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            textId = R.string.Disconnect_My_Device,
            onClick = { handleEvent(MyDeviceEvent.DisconnectClick) })
    }
    ToolbarWithNameBack(
        backClick = { handleEvent(MyDeviceEvent.BackButtonClick) },
        screenName = stringResource(id = R.string.My_Device),
        modifier = Modifier.statusBarsPadding()
    )
    if (showDisconnectConfirmation()) {
        ConfirmPopup(
            title = stringResource(id = R.string.Are_you_sure),
            description = stringResource(id = R.string.Fitbit_disconnect_message),
            confirmButtonText = R.string.Disconnect,
            onConfirmClick = {
                handleEvent(MyDeviceEvent.DisconnectConfirmed)
            },
            onCancelClick = {
                handleEvent(MyDeviceEvent.PopupCancelButtonClick)
            })
    } else if(showForceDisconnectConfirmation()){
        ConfirmPopup(
            title = stringResource(id = R.string.Sorry),
            description = stringResource(id = R.string.Fitbit_force_disconnect_message),
            confirmButtonText = R.string.Disconnect,
            onConfirmClick = {
                handleEvent(MyDeviceEvent.ForceDisconnectConfirmed)
            },
            onCancelClick = {
                handleEvent(MyDeviceEvent.PopupCancelButtonClick)
            })
    }
}

@Preview()
@Composable
private fun ContentPrev() {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = typography,
    ) {

        Content(handleEvent = {}, showDisconnectConfirmation = { false }, showForceDisconnectConfirmation = { false })
    }
}