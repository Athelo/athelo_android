package com.athelohealth.mobile.presentation.ui.patient.symptomRecommendation

import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

data class RecommendationSymptomViewState(
    override val isLoading: Boolean = true,
    val name: String = "",
    val description: String = ""
) : BaseViewState

sealed interface RecommendationSymptomEvent : BaseEvent {
    object BackButtonClick : RecommendationSymptomEvent
}

sealed interface RecommendationSymptomEffect : BaseEffect {
    object ShowPrevScreen : RecommendationSymptomEffect
}