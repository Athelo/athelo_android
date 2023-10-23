package com.i2asolutions.athelo.presentation.ui.share.authorization.signInWithEmail

import com.i2asolutions.athelo.presentation.model.base.InputType
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class SignInWithEmailViewState(
    val username: String = "",
    override val isLoading: Boolean = false,
    val usernameError: Boolean,
    val passwordError: Boolean,
    val enableButton: Boolean,
) : BaseViewState

sealed interface SignInWithEmailEvent : BaseEvent {
    class InputValueChanged(val inputType: InputType) : SignInWithEmailEvent
    object SignInClicked : SignInWithEmailEvent
    object ForgotPasswordClick : SignInWithEmailEvent
    object BackButtonClick : SignInWithEmailEvent
    object ToSButtonClick : SignInWithEmailEvent
    object PPButtonClick : SignInWithEmailEvent

}

sealed interface SignInWithEmailEffect : BaseEffect {
    object GoBack : SignInWithEmailEffect
    object ShowHomeScreen : SignInWithEmailEffect
    class ShowForgotPassword(val email: String) : SignInWithEmailEffect
    class ShowRoleScreen(val initFlow: Boolean) : SignInWithEmailEffect

    object ShowAdditionalInformationScreen : SignInWithEmailEffect
    object ShowPrivacyPolicyScreen : SignInWithEmailEffect
    object ShowTermsOfUseScreen : SignInWithEmailEffect
}