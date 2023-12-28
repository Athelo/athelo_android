package com.athelohealth.mobile.presentation.ui.patient.symptomSummary

import com.athelohealth.mobile.presentation.model.health.Symptom
import com.athelohealth.mobile.presentation.model.health.SymptomSummary
import com.athelohealth.mobile.presentation.model.mySymptoms.MySymptomsListType
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

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