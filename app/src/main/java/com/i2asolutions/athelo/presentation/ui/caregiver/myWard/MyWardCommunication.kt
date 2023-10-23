package com.i2asolutions.athelo.presentation.ui.caregiver.myWard

import com.i2asolutions.athelo.presentation.model.patients.Patient
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class MyWardViewState(
    override val isLoading: Boolean = false,
    val patients: List<Patient> = emptyList(),
    val showPatientMoreOption: Patient? = null,
    val showPatientDeleteConfirmation: Patient? = null,
) : BaseViewState

sealed interface MyWardEvent : BaseEvent {
    class PatientClick(val patient: Patient) : MyWardEvent
    class SendMessageClick(val patient: Patient) : MyWardEvent
    object CancelClick : MyWardEvent
    class DeleteWardClick(val patient: Patient) : MyWardEvent
    class DeleteWardConfirmationClick(val patient: Patient) : MyWardEvent

    class ShowMoreOptionClick(val patient: Patient) : MyWardEvent
    object BackButtonClick : MyWardEvent
    object AddPatientButtonClick : MyWardEvent
}

sealed interface MyWardEffect : BaseEffect {
    object ShowPrevScreen : MyWardEffect
    object ShowInvitationScreen : MyWardEffect
    object ShowSelectRoleScreen : MyWardEffect
    class ShowDeletePatientScreen(val patient: Patient) : MyWardEffect
    class ShowPatientConversationScreen(val conversationId: Int) : MyWardEffect
}