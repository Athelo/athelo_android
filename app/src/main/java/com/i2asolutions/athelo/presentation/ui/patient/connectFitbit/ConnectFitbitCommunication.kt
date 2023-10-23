package com.i2asolutions.athelo.presentation.ui.patient.connectFitbit

import com.i2asolutions.athelo.presentation.model.connectFitbit.ConnectFitbitScreenType
import com.i2asolutions.athelo.presentation.model.member.User
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class ConnectFitbitViewState(
    override val isLoading: Boolean = false,
    val screenType: ConnectFitbitScreenType = ConnectFitbitScreenType.Empty,
    val currentUser: User = User(),
    val displaySkipButton: Boolean = false
) : BaseViewState

sealed interface ConnectFitbitEvent : BaseEvent {
    object ConnectButtonClick : ConnectFitbitEvent
    object SkipButtonClick : ConnectFitbitEvent
    object GoToActivityPageClick : ConnectFitbitEvent
    object MenuClick : ConnectFitbitEvent
    object BackButtonClick : ConnectFitbitEvent
    object MyProfileClick : ConnectFitbitEvent
}

sealed interface ConnectFitbitEffect : BaseEffect {
    object ShowPrevScreen : ConnectFitbitEffect
    class ShowConnectFitbitScreen(val url: String) : ConnectFitbitEffect
    object ShowActivityPageScreen : ConnectFitbitEffect
    object ShowHomePageScreen : ConnectFitbitEffect
    object ShowMenuScreen : ConnectFitbitEffect
    object ShowMyProfileScreen : ConnectFitbitEffect
}