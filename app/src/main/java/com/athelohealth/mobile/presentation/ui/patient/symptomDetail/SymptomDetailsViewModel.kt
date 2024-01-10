package com.athelohealth.mobile.presentation.ui.patient.symptomDetail

import androidx.lifecycle.SavedStateHandle
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.health.LoadSymptomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SymptomDetailsViewModel @Inject constructor(
    private val loadSymptomUseCase: LoadSymptomUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<SymptomDetailsEvent, SymptomDetailsEffect, SymptomDetailsViewState>(SymptomDetailsViewState()) {
    private val symptomId =
        SymptomDetailsFragmentArgs.fromSavedStateHandle(savedStateHandle).symptomId

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    override fun loadData() {
        launchRequest {
            val symptom = loadSymptomUseCase(symptomId)
            notifyStateChange(
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

}