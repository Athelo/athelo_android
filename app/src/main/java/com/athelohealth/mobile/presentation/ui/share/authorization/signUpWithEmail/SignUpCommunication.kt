package com.athelohealth.mobile.presentation.ui.share.authorization.signUpWithEmail

import com.athelohealth.mobile.presentation.model.base.InputType
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

data class SignUpViewState(
    override val isLoading: Boolean = false,
    val enableButton: Boolean = false,
    val usernameError: Boolean = false,
    val passwordError: Boolean = false,
    val confirmPasswordError: Boolean = false
) : BaseViewState

sealed interface SignUpEffect : BaseEffect {
    object ShowAdditionalInfoScreen : SignUpEffect
    object GoBack : SignUpEffect
    object ShowToSScreen : SignUpEffect
    object ShowPPScreen : SignUpEffect
}

sealed interface SignUpEvent : BaseEvent {
    object SignUpClick : SignUpEvent
    object BackButtonClick : SignUpEvent
    object ToSButtonClick : SignUpEvent
    object PPButtonClick : SignUpEvent
    data class InputValueChanged(val inputValue: InputType) : SignUpEvent
}