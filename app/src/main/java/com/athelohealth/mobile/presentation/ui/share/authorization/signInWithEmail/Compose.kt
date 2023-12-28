@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package com.athelohealth.mobile.presentation.ui.share.authorization.signInWithEmail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.athelohealth.mobile.R
import com.athelohealth.mobile.extensions.debugPrint
import com.athelohealth.mobile.presentation.model.base.InputType
import com.athelohealth.mobile.presentation.ui.base.*
import com.athelohealth.mobile.presentation.ui.theme.darkPurple
import com.athelohealth.mobile.presentation.ui.theme.textField

@Composable
fun SignInScreen(viewModel: SignInWithEmailViewModel) {
    val state = viewModel.state.collectAsState()
    BoxScreen(
        viewModel = viewModel,
        showProgressProvider = { state.value.isLoading },
        modifier = Modifier.navigationBarsPadding(),
    ) {
        val focusController = LocalFocusManager.current
        Column(Modifier.imePadding()) {
            Toolbar(
                modifier = Modifier,
                screenName = stringResource(id = R.string.Log_In),
                showBack = true,
                onBackClick = {
                    viewModel.handleEvent(SignInWithEmailEvent.BackButtonClick)
                })
            InputScrollSection(viewModel, state, focusController)
            Spacer(modifier = Modifier.weight(1f))
            MainButton(
                textId = R.string.Log_In,
                onClick = {
                    focusController.clearFocus()
                    viewModel.handleEvent(SignInWithEmailEvent.SignInClicked)
                },
                enableButton = state.value.enableButton.also { debugPrint(it) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TosPpText(
                Modifier.align(Alignment.CenterHorizontally),
                onTosClick = { viewModel.handleEvent(SignInWithEmailEvent.ToSButtonClick) },
                onPPClick = { viewModel.handleEvent(SignInWithEmailEvent.ToSButtonClick) })
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun InputScrollSection(
    viewModel: SignInWithEmailViewModel,
    state: State<SignInWithEmailViewState>,
    focusController: FocusManager
) = Column(Modifier.verticalScroll(rememberScrollState())) {
    InputTextField(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        labelText = stringResource(id = R.string.Email),
        imeAction = ImeAction.Next,
        onChange = {
            viewModel.handleEvent(
                SignInWithEmailEvent.InputValueChanged(InputType.Email(it.text))
            )
        },
        isErrorShown = state.value.usernameError,
        hint = stringResource(id = R.string.Enter_your_email),
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
        imeAction = ImeAction.Done,
        onChange = {
            viewModel.handleEvent(
                SignInWithEmailEvent.InputValueChanged(InputType.Password(it.text))
            )
        },
        keyboardActions = KeyboardActions(onDone = {
            viewModel.handleEvent(SignInWithEmailEvent.SignInClicked)
            focusController.clearFocus()
        }),
        isPasswordField = true,
        errorMessage = stringResource(id = R.string.Wrong_password),
        isErrorShown = state.value.passwordError,
        hint = stringResource(id = R.string.Enter_your_password),
        initialValue = "",
        singleLine = true,
    )
    Spacer(modifier = Modifier.height(24.dp))
    Button(
        modifier = Modifier
            .shadow(0.dp),
        onClick = {
            focusController.clearFocus()
            viewModel.handleEvent(SignInWithEmailEvent.ForgotPasswordClick)
        },
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Transparent,
            backgroundColor = Color.Transparent
        ),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp
        )
    ) {
        Text(
            text = stringResource(id = R.string.Forgot_Password),
            style = MaterialTheme.typography.textField.copy(
                color = darkPurple,
                textDecoration = TextDecoration.Underline
            )
        )
    }
}
