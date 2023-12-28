package com.athelohealth.mobile.presentation.ui.patient.symptomDetail

import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

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