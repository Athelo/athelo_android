package com.athelohealth.mobile.presentation.ui.share.heartRate

import com.athelohealth.mobile.presentation.model.activity.ActivityGraphScreen
import com.athelohealth.mobile.presentation.model.patients.Patient
import com.athelohealth.mobile.presentation.model.sleep.HistoryRange
import com.athelohealth.mobile.presentation.model.sleep.SleepDetailScreen.PeriodInfo
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

data class HeartRateViewState(
    override val isLoading: Boolean,
    val selectedRange: HistoryRange = HistoryRange.Day,
    val periodInfo: PeriodInfo? = null,
    val information: ActivityGraphScreen.ActivityInformation? = null,
    val monthlyInformation: ActivityGraphScreen.ActivityLineInformation? = null,
    val desc: String? = null,
    val patients: List<Patient> = emptyList(),
    val selectedPatient: Patient? = null,
) : BaseViewState

sealed interface HeartRateEvent : BaseEvent {
    class RangeChanged(val range: HistoryRange) : HeartRateEvent
    object BackClick : HeartRateEvent
    object RefreshData : HeartRateEvent
    object NextClicked : HeartRateEvent
    object PrevClicked : HeartRateEvent
    data class ChangePatient(val patient: Patient) : HeartRateEvent
}

sealed interface HeartRateEffect : BaseEffect {
    object GoBack : HeartRateEffect
    object ShowLostCaregiverScreen : HeartRateEffect
}