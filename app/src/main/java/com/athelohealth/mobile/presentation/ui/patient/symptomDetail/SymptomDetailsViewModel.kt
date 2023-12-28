package com.athelohealth.mobile.presentation.ui.patient.symptomDetail

import androidx.lifecycle.SavedStateHandle
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.health.LoadSymptomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SymptomDetailsViewModel @Inject constructor(
    private val loadSymptomUseCase: LoadSymptomUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<SymptomDetailsEvent, SymptomDetailsEffect>() {
    private val symptomId =
        SymptomDetailsFragmentArgs.fromSavedStateHandle(savedStateHandle).symptomId
    private var currentState = SymptomDetailsViewState()

    private val _state = MutableStateFlow(currentState)
    val state = _state.asStateFlow()

    override fun loadData() {
        launchRequest {
            val symptom = loadSymptomUseCase(symptomId)
            notifyStateChanged(
                currentState.copy(
                    isLoading = false,
                    name = symptom.name,
                    description = symptom.description ?: ""
                )
            )
        }
    }

    override fun handleEvent(event: SymptomDetailsEvent) {
        when (event) {
            SymptomDetailsEvent.BackButtonClick -> notifyEffectChanged(SymptomDetailsEffect.ShowPrevScreen)
            SymptomDetailsEvent.ChronologyClick -> notifyEffectChanged(SymptomDetailsEffect.ShowChronologyScreen)
        }
    }

    private fun notifyStateChanged(newState: SymptomDetailsViewState) {
        currentState = newState
        launchOnUI { _state.emit(currentState) }
    }
}