package com.athelohealth.mobile.presentation.ui.patient.symptomSummary

import com.athelohealth.mobile.presentation.model.health.SymptomSummary
import com.athelohealth.mobile.presentation.model.mySymptoms.MySymptomsListType
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.health.LoadSymptomsSummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MySymptomsViewModel @Inject constructor(
    private val loadMySymptomChronology: LoadSymptomsSummaryUseCase
) : BaseViewModel<MySymptomsEvent, MySymptomsEffect, MySymptomsViewState>(MySymptomsViewState()) {
    private val allSymptoms = mutableListOf<SymptomSummary>()
    private var nextUrl: String? = null

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }
    override fun loadData() {
        notifyStateChange(currentState.copy(isLoading = true))
        launchRequest {
            allSymptoms.clear()
            val symptoms = loadMySymptomChronology()
            if (symptoms.isNotEmpty())
                allSymptoms.addAll(symptoms)
            notifyStateChange(
                currentState.copy(
                    isLoading = false,
                    data = generateList(),
                )
            )
        }
    }

    override fun handleEvent(event: MySymptomsEvent) {
        when (event) {
            MySymptomsEvent.BackButtonClick -> notifyEffectChanged(MySymptomsEffect.ShowPrevScreen)
            is MySymptomsEvent.CellClick -> notifyEffectChanged(MySymptomsEffect.ShowChronologyScreen)
            is MySymptomsEvent.TabChanged -> {
                notifyStateChange(
                    currentState.copy(
                        selectedTab = event.tabClicked,
                        data = generateList(event.tabClicked)
                    )
                )
            }
            MySymptomsEvent.LoadNextPage -> {
                launchRequest {
                    if (!nextUrl.isNullOrBlank()) {
                        loadData()
                    }
                }
            }
        }
    }

    private fun generateList(type: MySymptomsListType = currentState.selectedTab) =
        if (type == MySymptomsListType.MostUsed)
            allSymptoms.asSequence()
                .sortedByDescending { it.count }
                .toList()
        else allSymptoms.asSequence()
            .sortedBy { it.symptom.name.lowercase() }
            .toList()

}