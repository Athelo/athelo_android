package com.i2asolutions.athelo.presentation.ui.patient.symptomInfo

import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class InfoSymptomViewState(
    override val isLoading: Boolean = false,
    val title: String = "",
    val comment: String = ""
) : BaseViewState

sealed interface InfoSymptomEvent : BaseEvent {
    object BackButtonClick : InfoSymptomEvent
    object RecommendationButtonClick : InfoSymptomEvent
}

sealed interface InfoSymptomEffect : BaseEffect {
    object ShowPrevScreen : InfoSymptomEffect
    class ShowRecommendationScreen(val symptomId: Int) : InfoSymptomEffect
}