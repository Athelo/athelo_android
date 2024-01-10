package com.athelohealth.mobile.presentation.ui.caregiver.invitationCode

import com.athelohealth.mobile.presentation.model.base.InputType
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.SetupPersonalConfigUseCase
import com.athelohealth.mobile.useCase.caregiver.ResendInvitationCodeUseCase
import com.athelohealth.mobile.useCase.caregiver.VerifyInvitationCodeUseCase
import com.athelohealth.mobile.useCase.patients.SendGlobalMessageUseCase
import com.athelohealth.mobile.utils.message.LocalMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InvitationCodeViewModel @Inject constructor(
    private val resendInvitationCode: ResendInvitationCodeUseCase,
    private val verifyInvitationCode: VerifyInvitationCodeUseCase,
    private val setupPersonalInfo: SetupPersonalConfigUseCase,
    private val sendGlobalMessage: SendGlobalMessageUseCase,
) : BaseViewModel<InvitationCodeEvent, InvitationCodeEffect, InvitationCodeViewState>(InvitationCodeViewState()) {
    override fun pauseLoadingState() {
        notifyStateChange(currentState.copy(isLoading = false))
    }

    private var invitationCode: String = ""

    override fun loadData() {}

    override fun handleEvent(event: InvitationCodeEvent) {
        when (event) {
            InvitationCodeEvent.BackButtonClick -> notifyEffectChanged(
                InvitationCodeEffect.ShowPrevScreen
            )
            is InvitationCodeEvent.InputValueChanged -> when (event.inputType) {
                is InputType.Pin -> {
                    invitationCode = event.inputType.value
                    notifyStateChange(currentState.copy(enableNextButton = validate(), pin = invitationCode))
                }
                else -> {/*Ignore other types*/
                }
            }
            InvitationCodeEvent.NextButtonClick -> sendRequest()
            InvitationCodeEvent.ResendCodeButtonClick -> sendResendRequest()
        }
    }

    override fun handleError(throwable: Throwable) {
        super.handleError(throwable)
        invitationCode = ""
        notifyStateChange(currentState.copy(pin = invitationCode, isLoading = false))
    }

    private fun sendRequest() = selfBlockRun {
        if (validate()) {
            launchRequest {
                val result = verifyInvitationCode(invitationCode)
                val profile = setupPersonalInfo()
                if (result) {
                    sendGlobalMessage(LocalMessage.Success("You accepted new ward"))
                    notifyEffectChanged(InvitationCodeEffect.ShowPrevScreen)
                } else {
                    invitationCode = ""
                    notifyStateChange(currentState.copy(pin = invitationCode))
                }
            }
        }
    }

    private fun sendResendRequest() = selfBlockRun {
        launchRequest {
            resendInvitationCode()
            successMessage("The new message with code has been send.")
        }
    }

    private fun validate() = invitationCode.length == 6
}