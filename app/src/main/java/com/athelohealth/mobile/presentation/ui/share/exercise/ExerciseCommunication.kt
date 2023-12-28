package com.athelohealth.mobile.presentation.ui.share.exercise

import com.athelohealth.mobile.presentation.model.activity.ActivityGraphScreen
import com.athelohealth.mobile.presentation.model.patients.Patient
import com.athelohealth.mobile.presentation.model.sleep.HistoryRange
import com.athelohealth.mobile.presentation.model.sleep.SleepDetailScreen.PeriodInfo
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

data class ExerciseViewState(
    override val isLoading: Boolean,
    val selectedRange: HistoryRange = HistoryRange.Day,
    val periodInfo: PeriodInfo? = null,
    val information: ActivityGraphScreen.ActivityInformation? = null,
    val desc: String? = null,
    val patients: List<Patient> = emptyList(),
    val selectedPatient: Patient? = null,
) : BaseViewState

sealed interface ExerciseEvent : BaseEvent {
    data class ChangePatient(val patient: Patient) : ExerciseEvent
    class RangeChanged(val range: HistoryRange) : ExerciseEvent
    object BackClick : ExerciseEvent
    object RefreshData : ExerciseEvent
    object NextClicked : ExerciseEvent
    object PrevClicked : ExerciseEvent
}

sealed interface ExerciseEffect : BaseEffect {
    object GoBack : ExerciseEffect
    object ShowLostCaregiverScreen : ExerciseEffect
}