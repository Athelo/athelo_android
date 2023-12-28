package com.athelohealth.mobile.presentation.ui.share.authorization.forgotPassword

import com.athelohealth.mobile.presentation.model.base.InputType
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

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