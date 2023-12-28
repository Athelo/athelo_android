package com.athelohealth.mobile.presentation.ui.patient.symptomAdd

import com.athelohealth.mobile.presentation.model.base.InputType
import com.athelohealth.mobile.presentation.model.health.Symptom
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

data class AddSymptomViewState(
    override val isLoading: Boolean = false,
    val title: String,
    val enableSaveButton: Boolean = true
) :
    BaseViewState

sealed interface AddSymptomEvent : BaseEvent {
    object CloseButtonClick :
        com.athelohealth.mobile.presentation.ui.patient.symptomAdd.AddSymptomEvent

    object SaveButtonClick :
        com.athelohealth.mobile.presentation.ui.patient.symptomAdd.AddSymptomEvent

    class InputValueChanged(val inputType: InputType) :
        com.athelohealth.mobile.presentation.ui.patient.symptomAdd.AddSymptomEvent
}

sealed interface AddSymptomEffect : BaseEffect {
    class ShowPrevScreen(val symptom: Symptom? = null) :
        com.athelohealth.mobile.presentation.ui.patient.symptomAdd.AddSymptomEffect
}