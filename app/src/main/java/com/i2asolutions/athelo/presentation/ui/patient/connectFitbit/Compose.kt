package com.i2asolutions.athelo.presentation.ui.patient.connectFitbit

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.model.connectFitbit.ConnectFitbitScreenType
import com.i2asolutions.athelo.presentation.ui.base.*
import com.i2asolutions.athelo.presentation.ui.theme.background
import com.i2asolutions.athelo.presentation.ui.theme.darkPurple
import com.i2asolutions.athelo.presentation.ui.theme.normalText

@Composable
fun ConnectFitbitScreen(viewModel: ConnectFitbitViewModel) {
    val state by viewModel.state.collectAsState()
    BoxScreen(
        viewModel = viewModel,
        showProgressProvider = { state.isLoading }
    ) {
        Column(Modifier.navigationBarsPadding()) {
            if (state.displaySkipButton) {
                ToolbarWithMyProfile(
                    userAvatar = state.currentUser.photo?.image5050,
                    userDisplayName = state.currentUser.displayName ?: "",
                    avatarClick = {
                        viewModel.handleEvent(ConnectFitbitEvent.MyProfileClick)
                    }
                )
            } else {
                ToolbarWithBackAndMyProfile(
                    userAvatar = state.currentUser.photo?.image5050,
                    userDisplayName = state.currentUser.displayName ?: "",
                    backClick = {
                        viewModel.handleEvent(ConnectFitbitEvent.BackButtonClick)
                    },
                    avatarClick = {
                        viewModel.handleEvent(ConnectFitbitEvent.MyProfileClick)
                    })
            }
//            when (state.screenType) {
//                ConnectFitbitScreenType.Landing -> LandingScreen(
//                    handleEvent = viewModel::handleEvent,
//                    displaySkipButton = state.displaySkipButton
//                )
//                ConnectFitbitScreenType.Success -> SuccessScreen(handleEvent = viewModel::handleEvent)
//                ConnectFitbitScreenType.Error -> ErrorScreen(handleEvent = viewModel::handleEvent)
//                ConnectFitbitScreenType.Empty -> {}
           // }
        }
    }
}

@Composable
private fun ColumnScope.LandingScreen(
    handleEvent: (ConnectFitbitEvent) -> Unit,
    displaySkipButton: Boolean
) {
    Text(
        text = stringResource(id = R.string.Connect_your_account_for_better_user_experience),
        style = MaterialTheme.typography.normalText.copy(textAlign = TextAlign.Center),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    )
    Image(
        painter = painterResource(id = R.drawable.not_connected),
        contentDescription = null,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .weight(1f),
        contentScale = ContentScale.FillWidth
    )
    MainButton(modifier = Modifier
        .padding(horizontal = 16.dp)
        .padding(bottom = 24.dp)
        .fillMaxWidth(),
        textId = R.string.Connect_Fitbit_account,
        onClick = {
            handleEvent(ConnectFitbitEvent.ConnectButtonClick)
        })
    if (displaySkipButton)
        SecondaryButton(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
                .fillMaxWidth(),
            text = R.string.Skip,
            onClick = { handleEvent(ConnectFitbitEvent.SkipButtonClick) },
            background = background,
            textColor = darkPurple
        )
}

@Composable
private fun ColumnScope.SuccessScreen(handleEvent: (ConnectFitbitEvent) -> Unit) {
    Text(
        text = stringResource(id = R.string.Fitbit_account_connected_successfully),
        style = MaterialTheme.typography.normalText,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    )
    Image(
        painter = painterResource(id = R.drawable.success_joined),
        contentDescription = null,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .weight(1f),
        contentScale = ContentScale.FillWidth
    )
    MainButton(modifier = Modifier
        .padding(16.dp)
        .padding(bottom = 24.dp)
        .fillMaxWidth(), textId = R.string.Go_To_Activity_Page,
        onClick = {
            handleEvent(ConnectFitbitEvent.GoToActivityPageClick)
        })
}

@Composable
private fun ColumnScope.ErrorScreen(handleEvent: (ConnectFitbitEvent) -> Unit) {
    Text(
        text = stringResource(id = R.string.Fitbit_account_connected_error),
        style = MaterialTheme.typography.normalText,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    )
    Image(
        painter = painterResource(id = R.drawable.error_joined),
        contentDescription = null,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .weight(1f),
        contentScale = ContentScale.FillWidth
    )
    MainButton(modifier = Modifier
        .padding(16.dp)
        .padding(bottom = 24.dp)
        .fillMaxWidth(), textId = R.string.Connect_Fitbit_account_Again,
        onClick = {
            handleEvent(ConnectFitbitEvent.ConnectButtonClick)
        })
}