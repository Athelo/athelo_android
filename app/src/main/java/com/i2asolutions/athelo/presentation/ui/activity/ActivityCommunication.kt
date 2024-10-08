package com.i2asolutions.athelo.presentation.ui.activity

import com.i2asolutions.athelo.presentation.model.activity.ActivityScreen
import com.i2asolutions.athelo.presentation.model.member.User
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class ActivityViewState(
    override val isLoading: Boolean,
    val showNotConnected: Boolean = false,
    val currentUser: User = User(),
    val topHint: String = "",
    val stepsInformation: ActivityScreen.Steps? = null,
    val activityInformation: ActivityScreen.Activity? = null,
    val heartRateInformation: ActivityScreen.HeartRate? = null,
    val hrvInformation: ActivityScreen.HeartRateVariability? = null,
) : BaseViewState

sealed interface ActivityEvent : BaseEvent {
    object MenuClick : ActivityEvent
    object MyProfileClick : ActivityEvent
    object ConnectSmartWatchClick : ActivityEvent
    object RefreshData : ActivityEvent
    object StepsClick : ActivityEvent
    object ActivityClick : ActivityEvent
    object HeartRateClick : ActivityEvent
    object HrvClick : ActivityEvent
}

sealed interface ActivityEffect : BaseEffect {
    object ShowMenuScreen : ActivityEffect
    object ShowMenu : ActivityEffect
    object ShowConnectSmartWatchScreen : ActivityEffect
    object ShowMyProfileScreen : ActivityEffect
    object ShowStepsScreen : ActivityEffect
    object ShowActivityScreen : ActivityEffect
    object ShowHeartRateScreen : ActivityEffect
    object ShowHrvScreen : ActivityEffect
}