@file:OptIn(ExperimentalFoundationApi::class)

package com.i2asolutions.athelo.presentation.ui.changePassword

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.model.base.InputType
import com.i2asolutions.athelo.presentation.ui.base.BoxScreen
import com.i2asolutions.athelo.presentation.ui.base.InputTextField
import com.i2asolutions.athelo.presentation.ui.base.MainButton
import com.i2asolutions.athelo.presentation.ui.base.Toolbar
import com.i2asolutions.athelo.presentation.ui.theme.body1
import com.i2asolutions.athelo.presentation.ui.theme.darkPurple
import com.i2asolutions.athelo.presentation.ui.theme.gray

@Composable
fun ChangePasswordScreen(viewModel: ChangePasswordViewModel) {
    val state = viewModel.state.collectAsState()
    BoxScreen(
        viewModel = viewModel,
        showProgressProvider = { state.value.isLoading },
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        Content(
            onEvent = viewModel::handleEvent,
            enableButton = state.value.enableButton,
            currentPassword = state.value.currentPassword,
            newPassword = state.value.newPassword,
            repeatPassword = state.value.repeatNewPassword
        )
    }
}

@Composable
fun Content(
    enableButton: Boolean,
    currentPassword: String = "",
    newPassword: String = "",
    repeatPassword: String = "",
    onEvent: (ChangePasswordEvent) -> Unit
) {
    Column(Modifier.imePadding()) {
        Toolbar(
            showBack = true,
            onBackClick = {
                onEvent(ChangePasswordEvent.BackButtonClick)
            },
            screenName = stringResource(id = R.string.New_Password),
        )
        val focusController = LocalFocusManager.current
        ScrollContent(
            enableButton = enableButton,
            currentPassword = currentPassword,
            newPassword = newPassword,
            repeatPassword = repeatPassword,
            modifier = Modifier.weight(1f),
            focusController = focusController,
            onEvent = onEvent,
        )
        MainButton(
            textId = R.string.Save_New_Password,
            modifier = Modifier
                .padding(bottom = 24.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            enableButton = enableButton,
            onClick = {
                onEvent(ChangePasswordEvent.SendButtonClick)
            })
    }
}

@Composable
private fun ScrollContent(
    enableButton: Boolean,
    currentPassword: String = "",
    newPassword: String = "",
    repeatPassword: String = "",
    modifier: Modifier,
    focusController: FocusManager,
    onEvent: (ChangePasswordEvent) -> Unit
) = Column(
    modifier = modifier
        .verticalScroll(rememberScrollState())
        .padding(top = 24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(24.dp)
) {
    InputTextField(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        labelText = stringResource(id = R.string.Current_Password),
        imeAction = ImeAction.Next,
        onChange = {
            onEvent(ChangePasswordEvent.InputChanged(InputType.Password(it.text)))
        },
        keyboardActions = KeyboardActions(),
        isPasswordField = true,
        errorMessage = stringResource(id = R.string.Wrong_password),
        isErrorShown = false,
        hint = stringResource(id = R.string.Enter_your_current_password),
        initialValue = currentPassword
    )
    InputTextField(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        labelText = stringResource(id = R.string.New_Password),
        imeAction = ImeAction.Next,
        onChange = {
            onEvent(ChangePasswordEvent.InputChanged(InputType.NewPassword(it.text)))
        },
        keyboardActions = KeyboardActions(),
        isPasswordField = true,
        errorMessage = stringResource(id = R.string.Wrong_password),
        isErrorShown = false,
        hint = stringResource(id = R.string.Enter_your_new_password),
        initialValue = newPassword
    )
    InputTextField(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        labelText = stringResource(id = R.string.Confirm_New_Password),
        imeAction = ImeAction.Done,
        onChange = {
            onEvent(ChangePasswordEvent.InputChanged(InputType.ConfirmPassword(it.text)))
        },
        keyboardActions = KeyboardActions(onDone = {
            focusController.clearFocus()
            if (enableButton)
                onEvent(ChangePasswordEvent.SendButtonClick)
        }),
        isPasswordField = true,
        errorMessage = stringResource(id = R.string.Wrong_password),
        isErrorShown = false,
        hint = stringResource(id = R.string.Enter_your_new_password),
        initialValue = repeatPassword
    )
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.Your_password_must_be_different_from_the_old_one),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.body1.copy(darkPurple)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_questionmark),
            contentDescription = null,
            colorFilter = ColorFilter.tint(gray)
        )
    }
}

@Preview
@Composable
fun ContentPreview() {
    Content(onEvent = {}, enableButton = false)
}