package com.i2asolutions.athelo.presentation.ui.splash

import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class SplashViewState(
    override val isLoading: Boolean = true,
) : BaseViewState

sealed interface SplashEvent : BaseEvent {
    object InitApp : SplashEvent
    object ReloadData : SplashEvent
}

sealed interface SplashEffect : BaseEffect {
    object ShowHomeScreen : SplashEffect
    object ShowAuthorizationScreen : SplashEffect
    object ShowAdditionalInformationScreen : SplashEffect
    object ShowTutorialScreen : SplashEffect
    object CloseApp : SplashEffect
}