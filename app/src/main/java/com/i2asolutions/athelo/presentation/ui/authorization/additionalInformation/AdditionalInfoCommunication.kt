package com.i2asolutions.athelo.presentation.ui.authorization.additionalInformation

import com.i2asolutions.athelo.presentation.model.base.InputType
import com.i2asolutions.athelo.presentation.model.enums.EnumItem
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class AdditionalInfoViewState(
    override val isLoading: Boolean = false,
    val enableButton: Boolean,
    val displayNameError: Boolean = false,
    val userTypeError: Boolean = false,
    val selectedUserType: EnumItem = EnumItem.EMPTY,
    val userTypes: List<EnumItem> = emptyList()
) : BaseViewState

sealed interface AdditionalInfoEvent : BaseEvent {
    object SaveButtonClick : AdditionalInfoEvent

    class InputValueChanged(val inputType: InputType) : AdditionalInfoEvent
}

sealed interface AdditionalInfoEffect : BaseEffect {
    object ShowMainScreen : AdditionalInfoEffect
    object ShowConnectSmartWatchScreen : AdditionalInfoEffect
}