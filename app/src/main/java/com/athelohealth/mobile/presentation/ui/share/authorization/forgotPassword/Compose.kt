@file:OptIn(ExperimentalFoundationApi::class)

package com.athelohealth.mobile.presentation.ui.share.authorization.forgotPassword

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.athelohealth.mobile.R
import com.athelohealth.mobile.extensions.debugPrint
import com.athelohealth.mobile.presentation.model.base.InputType
import com.athelohealth.mobile.presentation.ui.base.*
import com.athelohealth.mobile.presentation.ui.theme.paragraph

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ForgotPasswordScreen(viewModel: ForgotPasswordViewModel) {
    val viewState = viewModel.viewState.collectAsState()
    BoxScreen(
        viewModel = viewModel,
        showProgressProvider = { viewState.value.isLoading },
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding()
    ) {
        Column {
            Toolbar(
                modifier = Modifier.fillMaxWidth(),
                screenName = stringResource(id = R.string.Reset_Password),
                showBack = true,
                onBackClick = {
                    viewModel.handleEvent(ForgotPasswordEvent.BackButtonClick)
                })
            val focusController = LocalFocusManager.current
            Column(modifier = Modifier.weight(1f)) {
                InputTextField(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    labelText = stringResource(id = R.string.Email),
                    imeAction = ImeAction.Done,
                    onChange = {
                        viewModel.handleEvent(
                            ForgotPasswordEvent.InputValueChanged(InputType.Email(it.text))
                        )
                    },
                    isErrorShown = viewState.value.emailError,
                    hint = stringResource(id = R.string.Enter_your_email),
                    initialValue = viewState.value.initialMessage,
                    errorMessage = stringResource(id = R.string.Wrong_email),
                    keyboardActions = KeyboardActions(onDone = {
                        viewModel.handleEvent(ForgotPasswordEvent.ForgotPasswordButtonClick)
                        focusController.clearFocus()
                    }),
                    keyboardType = KeyboardType.Email
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(id = R.string.Reset_Password_info),
                    style = MaterialTheme.typography.paragraph,
                    textAlign = TextAlign.Center
                )
            }
            MainButton(
                textId = R.string.Reset_Password,
                onClick = {
                    focusController.clearFocus()
                    viewModel.handleEvent(ForgotPasswordEvent.ForgotPasswordButtonClick)
                },
                enableButton = viewState.value.enableButton.also { debugPrint(it) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TosPpText(
                Modifier.align(Alignment.CenterHorizontally),
                onTosClick = { viewModel.handleEvent(ForgotPasswordEvent.ToSButtonClick) },
                onPPClick = { viewModel.handleEvent(ForgotPasswordEvent.PPButtonClick) })
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}