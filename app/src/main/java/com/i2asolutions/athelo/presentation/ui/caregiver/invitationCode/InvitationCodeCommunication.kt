package com.i2asolutions.athelo.presentation.ui.caregiver.invitationCode
import com.i2asolutions.athelo.presentation.model.base.InputType
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class InvitationCodeViewState(
    override val isLoading: Boolean = false,
    val enableNextButton: Boolean = false,
    val pin: String = ""
) : BaseViewState

sealed interface InvitationCodeEvent : BaseEvent {
    object BackButtonClick : InvitationCodeEvent
    object NextButtonClick : InvitationCodeEvent
    object ResendCodeButtonClick : InvitationCodeEvent
    class InputValueChanged(val inputType: InputType) : InvitationCodeEvent
}

sealed interface InvitationCodeEffect : BaseEffect {
    object ShowPrevScreen : InvitationCodeEffect
    object ShowMainScreen : InvitationCodeEffect
    object ShowAdditionalInfoScreen : InvitationCodeEffect
}
