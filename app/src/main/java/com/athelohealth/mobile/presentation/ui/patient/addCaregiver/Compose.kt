@file:OptIn(ExperimentalFoundationApi::class)

package com.athelohealth.mobile.presentation.ui.patient.addCaregiver

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.model.base.InputType
import com.athelohealth.mobile.presentation.model.enums.EnumItem
import com.athelohealth.mobile.presentation.ui.base.*
import com.athelohealth.mobile.presentation.ui.theme.normalText
import com.athelohealth.mobile.presentation.ui.theme.purple

@Composable
fun FindCaregiverScreen(viewModel: FindCaregiverViewModel) {
    val state by viewModel.state.collectAsState()
    BoxScreen(viewModel = viewModel, showProgressProvider = { state.isLoading }) {
        Content(
            handleEvent = viewModel::handleEvent,
            selectedItemProvider = { state.selectedRelationItem },
            relationsProvider = { state.relations },
            isButtonEnabledProvider = { state.enableFindButton },
            emailProvider = { state.email },
            nameProvider = { state.displayName }
        )
    }
}

@Composable
private fun Content(
    handleEvent: (FindCaregiverEvent) -> Unit = { },
    selectedItemProvider: () -> EnumItem = { EnumItem.EMPTY },
    relationsProvider: () -> List<EnumItem> = { emptyList() },
    isButtonEnabledProvider: () -> Boolean = { false },
    nameProvider: () -> String = { "" },
    emailProvider: () -> String = { " " }
) {
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding()
    ) {
        val focusController = LocalFocusManager.current
        ToolbarWithNameBack(
            backClick = { handleEvent(FindCaregiverEvent.BackButtonClick) },
            screenName = stringResource(id = R.string.Act_as_Patient)
        )
        Text(
            text = stringResource(id = R.string.Let_s_invite_your_caregiver),
            style = MaterialTheme.typography.normalText.copy(color = purple),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            InputTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                labelText = stringResource(id = R.string.User_Name),
                imeAction = ImeAction.Next,
                onChange = {
                    handleEvent(
                        FindCaregiverEvent.InputValueChanged(InputType.PersonName(it.text))
                    )
                },
                hint = stringResource(id = R.string.Enter_user_name_of_your_caregiver),
                initialValue = nameProvider(),
                keyboardActions = KeyboardActions(),
                keyboardType = KeyboardType.Email,
                singleLine = true
            )
            InputTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                labelText = stringResource(id = R.string.Email_Address),
                imeAction = ImeAction.Done,
                onChange = {
                    handleEvent(FindCaregiverEvent.InputValueChanged(InputType.Email(it.text)))
                },
                hint = stringResource(id = R.string.Enter_email_address_of_your_caregiver),
                initialValue = emailProvider(),
                keyboardActions = KeyboardActions(onDone = {
                    focusController.clearFocus(true)
                }),
                keyboardType = KeyboardType.Email,
                singleLine = true,
            )
            Column {
                DropDownMenuInput(
                    label = R.string.Who_are_you_to_this_person,
                    selectedItem = selectedItemProvider(),
                    data = relationsProvider(),
                    displayItems = 3,
                    onItemSelect = { item ->
                        handleEvent(
                            FindCaregiverEvent.InputValueChanged(InputType.DropDown(item.id))
                        )
                    })
            }
        }
        MainButton(
            textId = R.string.Invite_a_Caregiver,
            onClick = {
                focusController.clearFocus()
                handleEvent(FindCaregiverEvent.FindButtonClick)
            },
            enableButton = isButtonEnabledProvider()
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}