package com.athelohealth.mobile.presentation.ui.share.hrv

import com.athelohealth.mobile.presentation.model.activity.ActivityGraphScreen
import com.athelohealth.mobile.presentation.model.patients.Patient
import com.athelohealth.mobile.presentation.model.sleep.HistoryRange
import com.athelohealth.mobile.presentation.model.sleep.SleepDetailScreen.PeriodInfo
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState
import com.athelohealth.mobile.presentation.ui.share.activity.ActivityEffect

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