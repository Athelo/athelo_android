package com.athelohealth.mobile.presentation.ui.patient.addCaregiver

import com.athelohealth.mobile.presentation.model.base.InputType
import com.athelohealth.mobile.presentation.model.enums.EnumItem
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

data class FindCaregiverViewState(
    override val isLoading: Boolean = false,
    val enableFindButton: Boolean = false,
    val selectedRelationItem: EnumItem = EnumItem.EMPTY,
    val relations: List<EnumItem> = emptyList(),
    val displayName: String = "",
    val email: String = "",
) : BaseViewState

sealed interface FindCaregiverEvent : BaseEvent {
    class InputValueChanged(val inputType: InputType) : FindCaregiverEvent
    object FindButtonClick : FindCaregiverEvent
    object BackButtonClick : FindCaregiverEvent
}

sealed interface FindCaregiverEffect : BaseEffect {
    object ShowPrevScreen : FindCaregiverEffect
}