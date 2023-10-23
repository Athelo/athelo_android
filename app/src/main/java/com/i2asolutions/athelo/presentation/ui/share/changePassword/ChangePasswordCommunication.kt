package com.i2asolutions.athelo.presentation.ui.share.changePassword


import com.i2asolutions.athelo.presentation.model.base.InputType
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState


data class ChangePasswordViewState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val repeatNewPassword: String = "",
    override val isLoading: Boolean = false,
    val enableButton: Boolean = false,
) : BaseViewState

sealed interface ChangePasswordEvent : BaseEvent {
    object SendButtonClick : ChangePasswordEvent
    object BackButtonClick : ChangePasswordEvent
    class InputChanged(val inputType: InputType) : ChangePasswordEvent
}

sealed interface ChangePasswordEffect : BaseEffect {
    object ShowPrevScreen : ChangePasswordEffect
}