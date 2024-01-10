@file:OptIn(ExperimentalFoundationApi::class)

package com.athelohealth.mobile.presentation.ui.share.authorization.additionalInformation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.model.base.InputType
import com.athelohealth.mobile.presentation.ui.base.*
import com.athelohealth.mobile.presentation.ui.theme.body1
import com.athelohealth.mobile.presentation.ui.theme.darkPurple
import com.athelohealth.mobile.presentation.ui.theme.gray
import com.athelohealth.mobile.presentation.ui.theme.headline20

@Composable
fun ShowAdditionalInfoScreen(viewModel: AdditionalInfoViewModel) {
    val viewState = viewModel.viewState.collectAsState()
    BoxScreen(
        viewModel = viewModel,
        showProgressProvider = { viewState.value.isLoading },
        modifier = Modifier.navigationBarsPadding()
    ) {
        val focusController = LocalFocusManager.current
        Column {
            Toolbar(screenName = stringResource(id = R.string.Additional_Information))
            Text(
                text = stringResource(id = R.string.additional_info_welcome_header),
                style = MaterialTheme.typography.headline20.copy(color = darkPurple),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Text(
                text = stringResource(id = R.string.additional_info_welcome_subheader),
                style = MaterialTheme.typography.body1.copy(color = gray),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            )

            InputTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                labelText = stringResource(id = R.string.Display_Name),
                imeAction = ImeAction.Next,
                onChange = {
                    viewModel.handleEvent(
                        AdditionalInfoEvent.InputValueChanged(
                            InputType.PersonName(it.text)
                        )
                    )
                },
                isErrorShown = viewState.value.displayNameError,
                hint = stringResource(id = R.string.Enter_your_display_name),
                initialValue = "",
                errorMessage = stringResource(id = R.string.Not_empty),
                keyboardActions = KeyboardActions(onNext = {
                    focusController.clearFocus()
                }),
                keyboardType = KeyboardType.Text
            )
            Spacer(modifier = Modifier.height(24.dp))
            Column {
                DropDownMenuInput(
                    selectedItem = viewState.value.selectedUserType,
                    data = viewState.value.userTypes,
                    isErrorShown = viewState.value.userTypeError,
                    onItemSelect = { item ->
                        viewModel.handleEvent(
                            AdditionalInfoEvent.InputValueChanged(
                                InputType.DropDown(
                                    item.id
                                )
                            )
                        )
                    })
            }
            Spacer(modifier = Modifier.weight(1f))
            MainButton(
                textId = R.string.Save,
                onClick = {
                    focusController.clearFocus()
                    viewModel.handleEvent(AdditionalInfoEvent.SaveButtonClick)
                },
                enableButton = viewState.value.enableButton
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

