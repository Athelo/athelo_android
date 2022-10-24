package com.i2asolutions.athelo.presentation.ui.sleepDetails

import com.i2asolutions.athelo.presentation.model.sleep.SleepDetailScreen.*
import com.i2asolutions.athelo.presentation.model.sleep.HistoryRange
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class SleepDetailViewState(
    override val isLoading: Boolean,
    val selectedRange: HistoryRange = HistoryRange.Day,
    val periodInfo: PeriodInfo? = null,
    val dailyInformation: DailyInformation? = null,
    val weeklyInformation: WeeklyInformation? = null,
    val monthlyInformation: MonthlyInformation? = null,
    val sleepDesc: String? = null,
) : BaseViewState

sealed interface SleepDetailEvent : BaseEvent {
    class RangeChanged(val range: HistoryRange) : SleepDetailEvent
    object BackClick : SleepDetailEvent
    object RefreshData : SleepDetailEvent
    object NextClicked : SleepDetailEvent
    object PrevClicked : SleepDetailEvent
}

sealed interface SleepDetailEffect : BaseEffect {
    object GoBack : SleepDetailEffect
}