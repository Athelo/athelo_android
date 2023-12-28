package com.athelohealth.mobile.presentation.ui.patient.symptomChronology

import com.athelohealth.mobile.presentation.model.health.SymptomChronologyItem
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

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