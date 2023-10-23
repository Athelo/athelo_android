package com.i2asolutions.athelo.presentation.ui.patient.symptomSummary

import com.i2asolutions.athelo.presentation.model.health.Symptom
import com.i2asolutions.athelo.presentation.model.health.SymptomSummary
import com.i2asolutions.athelo.presentation.model.mySymptoms.MySymptomsListType
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class MySymptomsViewState(
    override val isLoading: Boolean = false,
    val data: List<SymptomSummary> = emptyList(),
    val selectedTab: MySymptomsListType = MySymptomsListType.All,
    val canLoadMore: Boolean = false
) : BaseViewState {
}

sealed interface MySymptomsEvent : BaseEvent {
    class TabChanged(val tabClicked: MySymptomsListType) : MySymptomsEvent
    class CellClick(val item: Symptom) : MySymptomsEvent
    object BackButtonClick : MySymptomsEvent
    object LoadNextPage : MySymptomsEvent
}

sealed interface MySymptomsEffect : BaseEffect {
    object ShowPrevScreen : MySymptomsEffect
    class ShowSymptomDetail(val symptomId: Int) : MySymptomsEffect
}