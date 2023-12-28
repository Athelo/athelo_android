package com.athelohealth.mobile.presentation.ui.caregiver.invitationCode
import com.athelohealth.mobile.presentation.model.base.InputType
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

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
