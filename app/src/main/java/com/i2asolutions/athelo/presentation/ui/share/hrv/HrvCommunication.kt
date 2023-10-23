package com.i2asolutions.athelo.presentation.ui.share.hrv

import com.i2asolutions.athelo.presentation.model.activity.ActivityGraphScreen
import com.i2asolutions.athelo.presentation.model.patients.Patient
import com.i2asolutions.athelo.presentation.model.sleep.HistoryRange
import com.i2asolutions.athelo.presentation.model.sleep.SleepDetailScreen.PeriodInfo
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState
import com.i2asolutions.athelo.presentation.ui.share.activity.ActivityEffect

data class HrvViewState(
    override val isLoading: Boolean,
    val selectedRange: HistoryRange = HistoryRange.Day,
    val periodInfo: PeriodInfo? = null,
    val information: ActivityGraphScreen.ActivityLineInformation? = null,
    val desc: String? = null,
    val patients: List<Patient> = emptyList(),
    val selectedPatient: Patient? = null,
) : BaseViewState

sealed interface HrvEvent : BaseEvent {
    class RangeChanged(val range: HistoryRange) : HrvEvent
    object BackClick : HrvEvent
    object RefreshData : HrvEvent
    object NextClicked : HrvEvent
    object PrevClicked : HrvEvent
    data class ChangePatient(val patient: Patient): HrvEvent
}

sealed interface HrvEffect : BaseEffect {
    object GoBack : HrvEffect
    object ShowSelectRoleScreen : HrvEffect
}