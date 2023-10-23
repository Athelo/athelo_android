package com.i2asolutions.athelo.presentation.ui.patient.symptomDetail

import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class SymptomDetailsViewState(
    override val isLoading: Boolean = true,
    val name: String = "",
    val description: String = ""
) : BaseViewState

sealed interface SymptomDetailsEvent : BaseEvent {
    object BackButtonClick : SymptomDetailsEvent
    object ChronologyClick : SymptomDetailsEvent
}

sealed interface SymptomDetailsEffect : BaseEffect {
    object ShowPrevScreen : SymptomDetailsEffect
    object ShowChronologyScreen : SymptomDetailsEffect
}