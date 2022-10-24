package com.i2asolutions.athelo.presentation.ui.symptomChronology

import com.i2asolutions.athelo.presentation.model.health.SymptomChronology
import com.i2asolutions.athelo.presentation.model.health.SymptomChronologyItem
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class SymptomChronologyViewState(
    override val isLoading: Boolean = true,
    val items: List<SymptomChronologyItem> = emptyList(),
    val canLoadMore: Boolean = false
) : BaseViewState

sealed interface SymptomChronologyEvent : BaseEvent {
    object LoadNextPage : SymptomChronologyEvent
    object LoadFirstPage : SymptomChronologyEvent
    object BackButtonClick : SymptomChronologyEvent
}

sealed interface SymptomChronologyEffect : BaseEffect {
    object ShowPrevScreen : SymptomChronologyEffect
}