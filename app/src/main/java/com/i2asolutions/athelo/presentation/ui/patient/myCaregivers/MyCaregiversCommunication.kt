package com.i2asolutions.athelo.presentation.ui.patient.myCaregivers

import com.i2asolutions.athelo.presentation.model.caregiver.Caregiver
import com.i2asolutions.athelo.presentation.model.caregiver.Invitation
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class MyCaregiversViewState(
    val selectedType: MyCaregiverListType = MyCaregiverListType.MyCaregivers,
    override val isLoading: Boolean = false,
    val caregivers: List<Caregiver> = emptyList(),
    val invitations: List<Invitation> = emptyList(),
    val showCaregiverMoreOption: Caregiver? = null,
    val showCaregiverDeleteConfirmation: Caregiver? = null,
    val showInvitationDeleteConfirmation: Invitation? = null,
    val canLoadMore: Boolean = false,
) : BaseViewState

sealed interface MyCaregiversEvent : BaseEvent {
    class CaregiverClick(val caregiver: Caregiver) : MyCaregiversEvent
    class SendMessageClick(val caregiver: Caregiver) : MyCaregiversEvent
    object CancelClick : MyCaregiversEvent
    class DeleteCaregiverClick(val caregiver: Caregiver) : MyCaregiversEvent
    class DeleteCaregiverConfirmationClick(val caregiver: Caregiver) : MyCaregiversEvent
    class DeleteInvitationClick(val invitation: Invitation) : MyCaregiversEvent
    class DeleteInvitationConfirmationClick(val invitation: Invitation) : MyCaregiversEvent

    class ShowMoreOptionClick(val caregiver: Caregiver) : MyCaregiversEvent
    class ListTypeButtonClick(val type: MyCaregiverListType) : MyCaregiversEvent
    object BackButtonClick : MyCaregiversEvent
    object AddCaregiverButtonClick : MyCaregiversEvent
    object LoadNextPage : MyCaregiversEvent
}

sealed interface MyCaregiversEffect : BaseEffect {
    object ShowPrevScreen : MyCaregiversEffect
    object ShowInvitationScreen : MyCaregiversEffect
    class ShowCaregiverConversationScreen(val conversationId: Int) : MyCaregiversEffect
}