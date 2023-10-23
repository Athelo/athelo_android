package com.i2asolutions.athelo.presentation.ui.patient.symptomSummary

import com.i2asolutions.athelo.presentation.model.health.SymptomSummary
import com.i2asolutions.athelo.presentation.model.mySymptoms.MySymptomsListType
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.health.LoadSymptomsSummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MySymptomsViewModel @Inject constructor(
    private val loadMySymptomChronology: LoadSymptomsSummaryUseCase
) : BaseViewModel<MySymptomsEvent, MySymptomsEffect>() {
    private val allSymptoms = mutableListOf<SymptomSummary>()
    private var currentState = MySymptomsViewState()
    private var nextUrl: String? = null

    private val _state = MutableStateFlow(currentState)
    val state = _state.asStateFlow()

    override fun handleError(throwable: Throwable) {
        notifyStateChanged(currentState.copy(isLoading = false))
        super.handleError(throwable)
    }

    override fun loadData() {
        notifyStateChanged(currentState.copy(isLoading = true))
        launchRequest {
            allSymptoms.clear()
            val symptoms = loadMySymptomChronology()
            if (symptoms.isNotEmpty())
                allSymptoms.addAll(symptoms)
            notifyStateChanged(
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
            is MySymptomsEvent.CellClick -> notifyEffectChanged(
                MySymptomsEffect.ShowSymptomDetail(
                    event.item.id
                )
            )
            is MySymptomsEvent.TabChanged -> {
                notifyStateChanged(
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

    private fun notifyStateChanged(newState: MySymptomsViewState) {
        currentState = newState
        launchOnUI { _state.emit(currentState) }
    }
}