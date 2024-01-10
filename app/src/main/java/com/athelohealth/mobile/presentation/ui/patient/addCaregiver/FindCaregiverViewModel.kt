package com.athelohealth.mobile.presentation.ui.patient.addCaregiver

import com.athelohealth.mobile.presentation.model.base.InputType
import com.athelohealth.mobile.presentation.model.enums.EnumItem
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.caregiver.LoadRelationsUseCase
import com.athelohealth.mobile.useCase.caregiver.SendInvitationCodeUseCase
import com.athelohealth.mobile.useCase.patients.SendGlobalMessageUseCase
import com.athelohealth.mobile.utils.message.LocalMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FindCaregiverViewModel @Inject constructor(
    private val loadRelations: LoadRelationsUseCase,
    private val sendInvitation: SendInvitationCodeUseCase,
    private val sendGlobalMessage: SendGlobalMessageUseCase,
) : BaseViewModel<FindCaregiverEvent, FindCaregiverEffect, FindCaregiverViewState>(FindCaregiverViewState()) {
    private var username: String = ""
    private var emailAddress: String = ""
    private var relation: EnumItem = EnumItem.EMPTY
    private var relations: List<EnumItem> = emptyList()

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    override fun loadData() {
        notifyStateChange(currentState.copy(isLoading = true))
        launchRequest {
            relations = loadRelations()
            notifyStateChange(
                currentState.copy(
                    isLoading = false,
                    relations = relations,
                    selectedRelationItem = relation,
                    enableFindButton = validate()
                )
            )
        }
    }

    override fun handleEvent(event: FindCaregiverEvent) {
        when (event) {
            FindCaregiverEvent.BackButtonClick -> notifyEffectChanged(FindCaregiverEffect.ShowPrevScreen)
            FindCaregiverEvent.FindButtonClick -> {
                sendRequest()
            }
            is FindCaregiverEvent.InputValueChanged -> handleInputValue(event.inputType)
        }
    }

    private fun sendRequest() {
        if (validate()) {
            notifyStateChange(currentState.copy(isLoading = false))
            launchRequest {
                if (sendInvitation(username, emailAddress, relation.id)) {
                    sendGlobalMessage(LocalMessage.Success("Send Invitation Code to caregiver $username."))
                    notifyEffectChanged(FindCaregiverEffect.ShowPrevScreen)
                } else {
                    errorMessage("Could not invite caregiver with entered data")
                    clearInputAndUpdate()
                }
            }
        } else {
            errorMessage("Please fill all fields to send invitation for Caregiver.")
            clearInputAndUpdate()
        }
    }

    private fun clearInputAndUpdate() {
        relation = EnumItem.EMPTY
        username = ""
        emailAddress = ""
        notifyStateChange(
            currentState.copy(
                isLoading = false,
                displayName = username,
                email = emailAddress,
                selectedRelationItem = relation,
            )
        )
    }

    private fun handleInputValue(inputType: InputType) {
        when (inputType) {
            is InputType.DropDown -> relation =
                relations.firstOrNull { it.id == inputType.value } ?: EnumItem.EMPTY
            is InputType.PersonName -> username = inputType.value
            is InputType.Email -> emailAddress = inputType.value
            else -> {
                /* Ignore other types*/
            }
        }
        notifyStateChange(
            currentState.copy(
                selectedRelationItem = relation,
                enableFindButton = validate(),
                displayName = username,
                email = emailAddress,
            )
        )
    }

    private fun validate(): Boolean =
        relation.id != EnumItem.EMPTY.id && username.isNotBlank() && emailAddress.isNotBlank()
}