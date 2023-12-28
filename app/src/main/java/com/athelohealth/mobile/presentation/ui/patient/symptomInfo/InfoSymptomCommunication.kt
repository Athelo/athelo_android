package com.athelohealth.mobile.presentation.ui.patient.symptomInfo

import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

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