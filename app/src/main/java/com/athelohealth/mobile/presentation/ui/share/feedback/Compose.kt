@file:OptIn(ExperimentalFoundationApi::class)

package com.athelohealth.mobile.presentation.ui.share.feedback

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.model.base.InputType
import com.athelohealth.mobile.presentation.model.enums.EnumItem
import com.athelohealth.mobile.presentation.ui.base.*
import com.athelohealth.mobile.presentation.ui.theme.darkPurple
import com.athelohealth.mobile.presentation.ui.theme.paragraph

@Composable
fun FeedbackScreen(viewModel: FeedbackViewModel) {
    val viewState = viewModel.viewState.collectAsState()
    BoxScreen(
        viewModel = viewModel,
        showProgressProvider = { viewState.value.isLoading },
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        Content(
            options = viewState.value.options,
            handleEvent = viewModel::handleEvent,
            enableButton = viewState.value.enableSendButton,
            selectedItem = viewState.value.selectedOption,
            initMessageText = viewState.value.message
        )
    }
}

@Composable
fun Content(
    options: List<EnumItem>,
    selectedItem: EnumItem = EnumItem.EMPTY,
    initMessageText: String = "",
    handleEvent: (FeedbackEvent) -> Unit,
    enableButton: Boolean
) {
    Column(Modifier.imePadding()) {
        val focusManager = LocalFocusManager.current
        Toolbar(showBack = true, onBackClick = {
            handleEvent(FeedbackEvent.BackButtonClick)
        }, screenName = stringResource(id = R.string.Ask_Athelo))
        Text(
            text = stringResource(id = R.string.Send_Your_Feedback_Or_Question),
            style = MaterialTheme.typography.paragraph.copy(
                color = darkPurple,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier
                .padding(vertical = 24.dp, horizontal = 44.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
        ) {
            Column {
                DropDownMenuInput(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp),
                    label = R.string.Topic,
                    selectedItem= selectedItem,
                    data = options,
                    onItemSelect = {
                        handleEvent(FeedbackEvent.InputValueChanged(InputType.DropDown(it.id)))
                    },
                    displayItems = 3
                )
            }

            InputTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp, top = 16.dp)
                    .fillMaxWidth(),
                labelText = stringResource(id = R.string.Message),
                imeAction = ImeAction.Done,
                onChange = {
                    handleEvent(FeedbackEvent.InputValueChanged(InputType.Text(it.text)))
                },
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    handleEvent(FeedbackEvent.SendButtonClick)
                }),
                hint = stringResource(id = R.string.Enter_your_message),
                initialValue = initMessageText
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        MainButton(
            textId = R.string.Send_Feedback,
            enableButton = enableButton,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .fillMaxWidth(),
            onClick = {
                focusManager.clearFocus()
                handleEvent(FeedbackEvent.SendButtonClick)
            },
        )
    }
}

@Preview
@Composable
fun ContentPreview() {
    Content(
        options = listOf(
            EnumItem("1", "Option 1"),
            EnumItem("1", "Option 3"),
            EnumItem("1", "Option 4"),
            EnumItem("1", "Option 5")
        ), handleEvent = {}, enableButton = true
    )
}