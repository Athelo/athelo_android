package com.i2asolutions.athelo.presentation.ui.share.splash

import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class SplashViewState(
    override val isLoading: Boolean = true,
    val showPin: Boolean = true,
) : BaseViewState

sealed interface SplashEvent : BaseEvent {
    data class EnterPin(val pin: String) : SplashEvent
    object InitApp : SplashEvent
    object ReloadData : SplashEvent
}

sealed interface SplashEffect : BaseEffect {
    object ShowHomeScreen : SplashEffect
    object ShowAuthorizationScreen : SplashEffect
    object ShowAdditionalInformationScreen : SplashEffect
    object ShowTutorialScreen : SplashEffect
    data class ShowActAsScreen(val initFlow: Boolean) : SplashEffect
    object CloseApp : SplashEffect
}