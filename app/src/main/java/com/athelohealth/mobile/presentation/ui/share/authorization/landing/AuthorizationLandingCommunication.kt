package com.athelohealth.mobile.presentation.ui.share.authorization.landing

import android.content.Context
import androidx.activity.result.ActivityResult
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

sealed interface AuthorizationLandingEvent : BaseEvent {
    object SignInWithEmailClick : AuthorizationLandingEvent
    object SignUpWithEmailClick : AuthorizationLandingEvent
    object SignWithGoogleClick : AuthorizationLandingEvent
    object SignWithAppleClick : AuthorizationLandingEvent
    object SignWithFacebookClick : AuthorizationLandingEvent
    object SignWithTwitterClick : AuthorizationLandingEvent
    object TosLinkClick : AuthorizationLandingEvent
    object PPLinkClick : AuthorizationLandingEvent
    class TabClick(val index: Int) : AuthorizationLandingEvent
    class SignWithGoogleResult(val context: Context, val result: ActivityResult) :
        AuthorizationLandingEvent
}

sealed interface AuthorizationLandingEffect : BaseEffect {
    object ShowAdditionalInformationScreen : AuthorizationLandingEffect
    object ShowHomeScreen : AuthorizationLandingEffect
    object ShowSignInWithEmailScreen : AuthorizationLandingEffect
    object ShowSignUpWithEmailScreen : AuthorizationLandingEffect
    object ShowSignWithAppleScreen : AuthorizationLandingEffect
    object ShowSignWithGoogleScreen : AuthorizationLandingEffect
    object ShowSignWithFacebookScreen : AuthorizationLandingEffect
    object ShowSignWithTwitterScreen : AuthorizationLandingEffect
    object ShowPrivacyPolicyScreen : AuthorizationLandingEffect
    object ShowTermsOfUseScreen : AuthorizationLandingEffect
}

data class AuthorizationLandingViewState(
    override val isLoading: Boolean,
    val selectedTabIndex: Int
) : BaseViewState