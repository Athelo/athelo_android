@file:OptIn(ExperimentalFoundationApi::class)

package com.athelohealth.mobile.presentation.ui.share.editProfile

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.athelohealth.mobile.R
import com.athelohealth.mobile.extensions.getImageRotation
import com.athelohealth.mobile.extensions.phoneFilter
import com.athelohealth.mobile.presentation.model.base.InputType
import com.athelohealth.mobile.presentation.model.enums.EnumItem
import com.athelohealth.mobile.presentation.model.member.User
import com.athelohealth.mobile.presentation.ui.base.*
import com.athelohealth.mobile.presentation.ui.theme.background
import com.athelohealth.mobile.presentation.ui.theme.disableGray
import com.athelohealth.mobile.presentation.ui.theme.purple
import com.athelohealth.mobile.presentation.ui.theme.white
import com.athelohealth.mobile.utils.PhoneMaskVisualTransformation

@Composable
fun EditProfileScreen(viewModel: EditProfileViewModel) {
    val viewState = viewModel.viewState.collectAsState()
    val focusManager = LocalFocusManager.current
    BoxScreen(
        viewModel = viewModel,
        showProgressProvider = { viewState.value.isLoading },
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .imePadding(),
        ) {
            Toolbar(
                screenName = stringResource(id = R.string.Personal_Information),
                showBack = true,
                onBackClick = {
                    viewModel.handleEvent(EditProfileEvent.BackButtonClick)
                })
            ScrollContent(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f),
                image = viewState.value.tmpUserImage ?: viewState.value.user.photo?.image100100
                ?: R.drawable.ic_avatar_placeholder,
                onEvent = {
                    viewModel.handleEvent(it)
                },
                editMode = viewState.value.editMode,
                selectedItemId = viewState.value.treatmentStatus,
                dropDownData = viewState.value.treatmentTypes,
                user = viewState.value.user,
                selectedBirthDate = viewState.value.selectedBirthdate
            )
            ButtonsContent(
                hideRequestButton = viewState.value.hideRequestPasswordButton,
                onEvent = {
                    focusManager.clearFocus()
                    viewModel.handleEvent(it)
                },
                editMode = viewState.value.editMode,
            )
        }
    }
}

@Composable
fun ScrollContent(
    modifier: Modifier = Modifier,
    image: Any = R.drawable.ic_avatar_placeholder,
    onEvent: (EditProfileEvent) -> Unit = {},
    user: User = User(),
    editMode: Boolean = false,
    selectedItemId: EnumItem = EnumItem.EMPTY,
    dropDownData: List<EnumItem> = emptyList(),
    selectedBirthDate: String? = null,
) {
    Column(
        modifier = modifier.padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val rotation = if (image is Uri) image.path?.getImageRotation() ?: 0f else 0f
        Image(
            painter = rememberAsyncImagePainter(
                model = image
            ),
            contentDescription = "",
            modifier = Modifier
                .padding(16.dp)
                .size(96.dp)
                .clip(CircleShape)
                .rotate(rotation),
            contentScale = ContentScale.Crop
        )
        if (editMode)
            SecondaryButton(
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .wrapContentWidth(),
                text = R.string.Upload_Picture,
                onClick = { onEvent(EditProfileEvent.UploadPictureClick) },
                background = background,
                border = purple,
            )
        InputTextField(
            modifier = Modifier.padding(bottom = 24.dp),
            labelText = stringResource(id = R.string.Name),
            imeAction = ImeAction.Done,
            onChange = { onEvent(EditProfileEvent.InputChanged(InputType.PersonName(it.text))) },
            keyboardActions = KeyboardActions(),
            hint = "",
            singleLine = true,
            initialValue = user.displayName ?: "",
            disabledBackgroundColor = white,
            readOnly = !editMode,
        )
        InputTextField(
            modifier = Modifier.padding(bottom = 24.dp),
            labelText = stringResource(id = R.string.Email),
            imeAction = ImeAction.Next,
            onChange = { onEvent(EditProfileEvent.InputChanged(InputType.Email(it.text))) },
            keyboardActions = KeyboardActions(),
            hint = "",
            singleLine = true,
            disabledBackgroundColor = disableGray,
            readOnly = true,
            initialValue = user.email ?: "",
            hideShadow = true,
        )
        DropDownMenuInput(
            modifier = Modifier.padding(bottom = 24.dp),
            selectedItem = selectedItemId,
            data = dropDownData,
            readOnly = !editMode,
            onItemSelect = {
                onEvent(EditProfileEvent.InputChanged(InputType.DropDown(it.id)))
            }
        )

        InputTextField(
            modifier = Modifier
                .padding(bottom = 24.dp),
            labelText = stringResource(id = R.string.Phone_number),
            imeAction = ImeAction.Done,
            textFormatter = { it.copy(it.text.phoneFilter()) },
            onChange = { onEvent(EditProfileEvent.InputChanged(InputType.PhoneNumber(it.text))) },
            keyboardActions = KeyboardActions(),
            hint = stringResource(id = R.string.Enter_your_phone_number),
            disabledBackgroundColor = white,
            readOnly = !editMode,
            singleLine = true,
            visualTransformation = PhoneMaskVisualTransformation(),
            initialValue = user.phone ?: "",
            keyboardType = KeyboardType.Phone,
            isPasswordField = false
        )

        DropDownCalendarInput(
            modifier = Modifier.padding(bottom = 24.dp),
            label = R.string.Month_and_your_of_birth,
            initValue = selectedBirthDate,
            onItemSelect = { onEvent(EditProfileEvent.InputChanged(InputType.Calendar(it.serverDate))) },
            readOnly = !editMode,
        )
    }

}

@Composable
fun ButtonsContent(
    editMode: Boolean = false,
    hideRequestButton: Boolean = false,
    enableSaveButton: Boolean = true,
    onEvent: (EditProfileEvent) -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(bottom = 16.dp, top = 16.dp)
    ) {
        if (editMode) {
            MainButton(
                textId = R.string.Save_Personal_Information,
                enableButton = enableSaveButton,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onEvent(EditProfileEvent.SaveButtonClick)
                })
            if (!hideRequestButton) {
                SecondaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = R.string.Request_Password_Reset,
                    textColor = purple,
                    border = purple,
                    onClick = { onEvent(EditProfileEvent.RequestPasswordResetClick) },
                    background = background,
                )
            }
        } else {
            MainButton(
                textId = R.string.Edit_Personal_Information,
                enableButton = true,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onEvent(EditProfileEvent.EditButtonClick)
                },
            )
        }
    }
}

@Preview
@Composable
fun ScrollContentPreview() {
    ScrollContent()
}

@Preview
@Composable
fun ButtonPreview() {
    ButtonsContent()
}

@Preview
@Composable
fun EditModeButtonPreview() {
    ButtonsContent(editMode = true)
}