package com.i2asolutions.athelo.presentation.ui.exercise

import com.i2asolutions.athelo.presentation.model.activity.ActivityGraphScreen
import com.i2asolutions.athelo.presentation.model.sleep.SleepDetailScreen.*
import com.i2asolutions.athelo.presentation.model.sleep.HistoryRange
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class ExerciseViewState(
    override val isLoading: Boolean,
    val selectedRange: HistoryRange = HistoryRange.Day,
    val periodInfo: PeriodInfo? = null,
    val information: ActivityGraphScreen.ActivityInformation? = null,
    val desc: String? = null,
) : BaseViewState

sealed interface ExerciseEvent : BaseEvent {
    class RangeChanged(val range: HistoryRange) : ExerciseEvent
    object BackClick : ExerciseEvent
    object RefreshData : ExerciseEvent
    object NextClicked : ExerciseEvent
    object PrevClicked : ExerciseEvent
}

sealed interface ExerciseEffect : BaseEffect {
    object GoBack : ExerciseEffect
}