package com.i2asolutions.athelo.presentation.ui.heartRate

import com.i2asolutions.athelo.presentation.model.activity.ActivityGraphScreen
import com.i2asolutions.athelo.presentation.model.sleep.SleepDetailScreen.*
import com.i2asolutions.athelo.presentation.model.sleep.HistoryRange
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class HeartRateViewState(
    override val isLoading: Boolean,
    val selectedRange: HistoryRange = HistoryRange.Day,
    val periodInfo: PeriodInfo? = null,
    val information: ActivityGraphScreen.ActivityInformation? = null,
    val monthlyInformation: ActivityGraphScreen.ActivityLineInformation? = null,
    val desc: String? = null,
) : BaseViewState

sealed interface HeartRateEvent : BaseEvent {
    class RangeChanged(val range: HistoryRange) : HeartRateEvent
    object BackClick : HeartRateEvent
    object RefreshData : HeartRateEvent
    object NextClicked : HeartRateEvent
    object PrevClicked : HeartRateEvent
}

sealed interface HeartRateEffect : BaseEffect {
    object GoBack : HeartRateEffect
}