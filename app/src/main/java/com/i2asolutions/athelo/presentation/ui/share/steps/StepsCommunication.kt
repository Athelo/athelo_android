package com.i2asolutions.athelo.presentation.ui.share.steps

import com.i2asolutions.athelo.presentation.model.activity.ActivityGraphScreen
import com.i2asolutions.athelo.presentation.model.patients.Patient
import com.i2asolutions.athelo.presentation.model.sleep.HistoryRange
import com.i2asolutions.athelo.presentation.model.sleep.SleepDetailScreen.PeriodInfo
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class StepsViewState(
    override val isLoading: Boolean,
    val selectedRange: HistoryRange = HistoryRange.Day,
    val periodInfo: PeriodInfo? = null,
    val information: ActivityGraphScreen.ActivityInformation? = null,
    val desc: String? = null,
    val patients: List<Patient> = emptyList(),
    val selectedPatient: Patient? = null,
) : BaseViewState

sealed interface StepsEvent : BaseEvent {
    class RangeChanged(val range: HistoryRange) : StepsEvent
    object BackClick : StepsEvent
    object RefreshData : StepsEvent
    object NextClicked : StepsEvent
    object PrevClicked : StepsEvent
    data class ChangePatient(val patient: Patient) : StepsEvent
}

sealed interface StepsEffect : BaseEffect {
    object GoBack : StepsEffect
    object ShowLostCaregiverScreen : StepsEffect
}