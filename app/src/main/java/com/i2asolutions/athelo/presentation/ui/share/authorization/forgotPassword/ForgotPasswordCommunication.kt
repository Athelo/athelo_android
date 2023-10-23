package com.i2asolutions.athelo.presentation.ui.share.authorization.forgotPassword

import com.i2asolutions.athelo.presentation.model.base.InputType
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

sealed interface ForgotPasswordEvent : BaseEvent {
    object ForgotPasswordButtonClick : ForgotPasswordEvent
    object BackButtonClick : ForgotPasswordEvent
    data class InputValueChanged(val inputType: InputType) : ForgotPasswordEvent
    object ToSButtonClick : ForgotPasswordEvent
    object PPButtonClick : ForgotPasswordEvent
}

sealed interface ForgotPasswordEffect : BaseEffect {
    object GoBack : ForgotPasswordEffect
    class DisplayMessage(val message: String) : ForgotPasswordEffect
}

data class ForgotPasswordViewState(
    override val isLoading: Boolean,
    val enableButton: Boolean,
    val emailError: Boolean,
    val initialMessage: String,
) :
    BaseViewState