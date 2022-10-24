package com.i2asolutions.athelo.presentation.ui.symptomAdd

import com.i2asolutions.athelo.presentation.model.base.InputType
import com.i2asolutions.athelo.presentation.model.health.Symptom
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class AddSymptomViewState(
    override val isLoading: Boolean = false,
    val title: String,
    val enableSaveButton: Boolean = true
) :
    BaseViewState

sealed interface AddSymptomEvent : BaseEvent {
    object CloseButtonClick : AddSymptomEvent
    object SaveButtonClick : AddSymptomEvent
    class InputValueChanged(val inputType: InputType) : AddSymptomEvent
}

sealed interface AddSymptomEffect : BaseEffect {
    class ShowPrevScreen(val symptom: Symptom? = null) : AddSymptomEffect
}