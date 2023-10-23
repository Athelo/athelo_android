package com.i2asolutions.athelo.presentation.ui.patient.caregiverList

import com.i2asolutions.athelo.presentation.model.caregiver.Caregiver
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class CaregiverListViewState(
    override val isLoading: Boolean = false,
    val myCaregivers: List<Caregiver> = emptyList(),
    val showCaregiverDeleteConfirmation: Caregiver? = null,
) : BaseViewState

sealed interface CaregiverListEvent : BaseEvent {
    class ConfirmationDeleteClick(val caregiver: Caregiver) : CaregiverListEvent
    class SelectCaregiverClick(val caregiver: Caregiver) : CaregiverListEvent
    object AddCaregiverClick : CaregiverListEvent
    object ProceedClick : CaregiverListEvent
    object BackButtonClick : CaregiverListEvent
    object CancelClick : CaregiverListEvent
}

sealed interface CaregiverListEffect : BaseEffect {
    object ShowPrevScreen : CaregiverListEffect
    object ShowSmartWatchScreen : CaregiverListEffect
    object ShowHomeScreen : CaregiverListEffect
    object ShowInvitationScreen : CaregiverListEffect
    class ShowRemoveConfirmationScreen(val caregiver: Caregiver) : CaregiverListEffect
}