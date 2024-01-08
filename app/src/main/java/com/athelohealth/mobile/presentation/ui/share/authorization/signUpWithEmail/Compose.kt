@file:OptIn(ExperimentalFoundationApi::class)

package com.athelohealth.mobile.presentation.ui.share.authorization.signUpWithEmail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.athelohealth.mobile.R
import com.athelohealth.mobile.extensions.debugPrint
import com.athelohealth.mobile.presentation.model.base.InputType
import com.athelohealth.mobile.presentation.ui.base.*

@Composable
fun SignUpScreen(viewModel: SignUpViewModel) {
    val state = viewModel.viewState.collectAsState()
    BoxScreen(
        viewModel = viewModel,
        showProgressProvider = { state.value.isLoading },
        modifier = Modifier.navigationBarsPadding(),
    ) {
        Column(Modifier.imePadding()) {
            Toolbar(
                modifier = Modifier,
                screenName = stringResource(id = R.string.Sign_Up),
                showBack = true,
                onBackClick = {
                    viewModel.handleEvent(SignUpEvent.BackButtonClick)
                })
            val focusController = LocalFocusManager.current
            InputScrollSection(viewModel, state, focusController)
            Spacer(modifier = Modifier.weight(1f))
            MainButton(
                textId = R.string.Register,
                onClick = {
                    focusController.clearFocus()
                    viewModel.handleEvent(SignUpEvent.SignUpClick)
                },
                enableButton = state.value.enableButton.also { debugPrint(it) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TosPpText(
                Modifier.align(Alignment.CenterHorizontally),
                onTosClick = { viewModel.handleEvent(SignUpEvent.ToSButtonClick) },
                onPPClick = { viewModel.handleEvent(SignUpEvent.ToSButtonClick) })
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun InputScrollSection(
    viewModel: SignUpViewModel,
    state: State<SignUpViewState>,
    focusController: FocusManager
) = Column(Modifier.verticalScroll(rememberScrollState())) {
    InputTextField(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        labelText = stringResource(id = R.string.Email),
        imeAction = ImeAction.Next,
        onChange = {
            viewModel.handleEvent(
                SignUpEvent.InputValueChanged(InputType.Email(it.text))
            )
        },
        isErrorShown = state.value.usernameError,
        hint = stringResource(id = R.string.Enter_your_email),
        initialValue = "",
        errorMessage = stringResource(id = R.string.Wrong_email),
        keyboardActions = KeyboardActions(),
        keyboardType = KeyboardType.Email,
        singleLine = true,
    )
    Spacer(modifier = Modifier.height(24.dp))
    InputTextField(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        labelText = stringResource(id = R.string.Password),
        imeAction = ImeAction.Next,
        onChange = {
            viewModel.handleEvent(
                SignUpEvent.InputValueChanged(InputType.Password(it.text))
            )
        },
        keyboardActions = KeyboardActions(),
        isPasswordField = true,
        errorMessage = stringResource(id = R.string.Wrong_password),
        isErrorShown = state.value.passwordError,
        hint = stringResource(id = R.string.Enter_your_password),
        initialValue = "",
        singleLine = true,
    )
    Spacer(modifier = Modifier.height(24.dp))
    InputTextField(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        labelText = stringResource(id = R.string.Confirm_Password),
        imeAction = ImeAction.Done,
        onChange = {
            viewModel.handleEvent(
                SignUpEvent.InputValueChanged(InputType.ConfirmPassword(it.text))
            )
        },
        keyboardActions = KeyboardActions(onDone = {
            viewModel.handleEvent(SignUpEvent.SignUpClick)
            focusController.clearFocus()
        }),
        isPasswordField = true,
        errorMessage = stringResource(id = R.string.Wrong_password),
        isErrorShown = state.value.confirmPasswordError,
        hint = stringResource(id = R.string.Confirm_your_password),
        initialValue = "",
        singleLine = true,
    )
}