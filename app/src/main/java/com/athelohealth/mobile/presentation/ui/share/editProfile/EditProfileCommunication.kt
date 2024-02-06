package com.athelohealth.mobile.presentation.ui.share.editProfile

import android.net.Uri
import com.athelohealth.mobile.presentation.model.base.InputType
import com.athelohealth.mobile.presentation.model.enums.EnumItem
import com.athelohealth.mobile.presentation.model.member.User
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState


data class EditProfileViewState(
    override val isLoading: Boolean = true,
    val enableSaveButton: Boolean = false,
    val editMode: Boolean = true,
    val enableEditMode: Boolean = false,
    val user: User = User(),
    val treatmentTypes: List<EnumItem>,
    val tmpUserImage: Uri? = null,
    val treatmentStatus: EnumItem = EnumItem.EMPTY,
    val selectedBirthdate: String? = null,
    val hideRequestPasswordButton: Boolean = false,
) : BaseViewState

sealed interface EditProfileEvent : BaseEvent {
    data class InputChanged(val inputType: InputType) : EditProfileEvent
    object SaveButtonClick : EditProfileEvent
    object EditButtonClick : EditProfileEvent
    object UploadPictureClick : EditProfileEvent
    object RequestPasswordResetClick : EditProfileEvent
    object BackButtonClick : EditProfileEvent
    object HideProgress : EditProfileEvent
    object ShowProgress : EditProfileEvent

    data class ImageSelected(val image: Uri) : EditProfileEvent
}

sealed interface EditProfileEffect : BaseEffect {
    object ShowImagePicker : EditProfileEffect
    object ShowResetPasswordScreen : EditProfileEffect
    object ShowPrevScreen : EditProfileEffect

}