package com.i2asolutions.athelo.presentation.ui.symptomRecommendation

import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

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