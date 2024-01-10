package com.athelohealth.mobile.presentation.ui.caregiver.invitationCode

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.model.base.InputType
import com.athelohealth.mobile.presentation.ui.base.BoxScreen
import com.athelohealth.mobile.presentation.ui.base.MainButton
import com.athelohealth.mobile.presentation.ui.base.ToolbarWithNameBack
import com.athelohealth.mobile.presentation.ui.base.menu.PinEditText
import com.athelohealth.mobile.presentation.ui.theme.AtheloTheme
import com.athelohealth.mobile.presentation.ui.theme.darkPurple
import com.athelohealth.mobile.presentation.ui.theme.headline20

@Composable
fun InvitationCodeScreen(viewModel: InvitationCodeViewModel) {
    val viewState by viewModel.viewState.collectAsState()
    BoxScreen(viewModel = viewModel, showProgressProvider = { viewState.isLoading }) {
        Content(
            handleEvent = viewModel::handleEvent,
            enableButtonProvider = { viewState.enableNextButton },
            pinProvider = { viewState.pin },
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    handleEvent: (InvitationCodeEvent) -> Unit,
    enableButtonProvider: () -> Boolean,
    pinProvider: () -> String
) {
    Column(horizontalAlignment = CenterHorizontally) {
        ToolbarWithNameBack(
            backClick = { handleEvent(InvitationCodeEvent.BackButtonClick) },
            screenName = stringResource(id = R.string.Invitation_code)
        )
        Text(
            text = stringResource(id = R.string.Enter_the_code_that_was_sent_to_you_along_with_the_invitation),
            style = MaterialTheme.typography.headline20.copy(
                color = darkPurple,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )
        PinEditText(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 40.dp, bottom = 24.dp)
                .heightIn(max = 56.dp),
            maxCount = 6,
            enteredPin = { pin ->
                handleEvent(InvitationCodeEvent.InputValueChanged(InputType.Pin(pin)))
            },
            initText = pinProvider()
        )
//       todo: Not information what should this button do - hide for now.
//        Text(
//            text = stringResource(id = R.string.I_dont_receive_my_code),
//            style = MaterialTheme.typography.button.copy(
//                color = darkPurple,
//                textAlign = TextAlign.Center,
//                textDecoration = TextDecoration.Underline
//            ), modifier = Modifier
//                .padding(horizontal = 16.dp)
//                .wrapContentWidth(CenterHorizontally)
//                .clickable {
//                    handleEvent(InvitationCodeEvent.ResendCodeButtonClick)
//                }
//        )
        Spacer(modifier = Modifier.weight(1f))
        MainButton(modifier = Modifier
            .padding(16.dp)
            .padding(bottom = 24.dp)
            .imePadding()
            .fillMaxWidth(),
            textId = R.string.Verify,
            enableButton = enableButtonProvider(),
            onClick = {
                handleEvent(InvitationCodeEvent.NextButtonClick)
            })
    }
}

@Preview
@Composable
fun ContentPreview() {
    AtheloTheme {
        Content(handleEvent = {}, enableButtonProvider = { false }, pinProvider = { "" })
    }
}