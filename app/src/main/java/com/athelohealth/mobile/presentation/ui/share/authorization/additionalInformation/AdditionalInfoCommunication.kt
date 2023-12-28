package com.athelohealth.mobile.presentation.ui.share.authorization.additionalInformation

import com.athelohealth.mobile.presentation.model.base.InputType
import com.athelohealth.mobile.presentation.model.enums.EnumItem
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

data class AdditionalInfoViewState(
    override val isLoading: Boolean = false,
    val enableButton: Boolean,
    val displayNameError: Boolean = false,
    val userTypeError: Boolean = false,
    val selectedUserType: EnumItem = EnumItem.EMPTY,
    val userTypes: List<EnumItem> = emptyList()
) : BaseViewState

sealed interface AdditionalInfoEvent : BaseEvent {
    object SaveButtonClick :
        com.athelohealth.mobile.presentation.ui.share.authorization.additionalInformation.AdditionalInfoEvent

    class InputValueChanged(val inputType: InputType) :
        com.athelohealth.mobile.presentation.ui.share.authorization.additionalInformation.AdditionalInfoEvent
}

sealed interface AdditionalInfoEffect : BaseEffect {
    object ShowMainScreen :
        com.athelohealth.mobile.presentation.ui.share.authorization.additionalInformation.AdditionalInfoEffect

    object ShowConnectSmartWatchScreen :
        com.athelohealth.mobile.presentation.ui.share.authorization.additionalInformation.AdditionalInfoEffect

    data class ShowActAsScreen(val initFlow: Boolean) :
        com.athelohealth.mobile.presentation.ui.share.authorization.additionalInformation.AdditionalInfoEffect
}