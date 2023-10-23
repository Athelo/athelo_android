package com.i2asolutions.athelo.presentation.ui.share.authorization.signUpWithEmail

import com.i2asolutions.athelo.presentation.model.base.InputType
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

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