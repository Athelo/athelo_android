package com.i2asolutions.athelo.presentation.ui.sleepSummary

import com.i2asolutions.athelo.presentation.model.member.User
import com.i2asolutions.athelo.presentation.model.sleep.SleepSummaryScreen.SleepInformation
import com.i2asolutions.athelo.presentation.model.sleep.SleepSummaryScreen.IdealSleep
import com.i2asolutions.athelo.presentation.model.sleep.SleepSummaryScreen.SleepResult
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class SleepViewState(
    override val isLoading: Boolean,
    val showNotConnected: Boolean = false,
    val currentUser: User = User(),
    val idealSleep: IdealSleep? = null,
    val sleepResult: SleepResult? = null,
    val sleepInformation: SleepInformation? = null,
) : BaseViewState

sealed interface SleepSummaryEvent : BaseEvent {
    class ReadArticleClick(val articleId: Int) : SleepSummaryEvent
    object MenuClick : SleepSummaryEvent
    object MyProfileClick : SleepSummaryEvent
    object ConnectSmartWatchClick : SleepSummaryEvent
    object RefreshData : SleepSummaryEvent
    object MoreClick : SleepSummaryEvent
}

sealed interface SleepEffect : BaseEffect {
    class ShowArticle(val articleId: Int) : SleepEffect
    object ShowMenuScreen : SleepEffect
    object ShowMyProfileScreen : SleepEffect
    object ShowConnectSmartWatchScreen : SleepEffect
    object ShowSleepDetailsScreen : SleepEffect
}