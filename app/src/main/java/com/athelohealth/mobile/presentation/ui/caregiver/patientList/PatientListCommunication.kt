package com.athelohealth.mobile.presentation.ui.caregiver.patientList

import com.athelohealth.mobile.presentation.model.patients.Patient
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

data class PatientListViewState(
    override val isLoading: Boolean = true,
    val patientList: List<Patient> = emptyList(),
    val enableProceedButton: Boolean = false,
    val selectedPatient: Patient? = null,
) : BaseViewState

sealed interface PatientListEvent : BaseEvent {
    data class PatientCellClick(val patient: Patient) : PatientListEvent
    object AddPatientClick: PatientListEvent
    object BackButtonClick: PatientListEvent
    object ProceedClick : PatientListEvent
}

sealed interface PatientListEffect : BaseEffect {
    object ShowPrevScreen: PatientListEffect
    object ShowHomeScreen: PatientListEffect
    object ShowSmartWatchScreen: PatientListEffect
    object ShowInvitationCodeScreen: PatientListEffect
}