package com.athelohealth.mobile.presentation.ui.patient.symptomRecommendation

import androidx.lifecycle.SavedStateHandle
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.health.LoadSymptomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecommendationSymptomViewModel @Inject constructor(
    private val loadSymptom: LoadSymptomUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<RecommendationSymptomEvent, RecommendationSymptomEffect, RecommendationSymptomViewState>(RecommendationSymptomViewState()) {
    private val symptomId: Int =
        RecommendationSymptomFragmentArgs.fromSavedStateHandle(savedStateHandle).symptomId

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    override fun loadData() {
        notifyStateChange(currentState.copy(isLoading = true))
        launchRequest {
            val symptom = loadSymptom(symptomId = symptomId)
            notifyStateChange(
                currentState.copy(
                    isLoading = false,
                    name = symptom.name,
                    description = symptom.description ?: ""
                )
            )
        }
    }

    override fun handleEvent(event: RecommendationSymptomEvent) {
        when (event) {
            RecommendationSymptomEvent.BackButtonClick -> notifyEffectChanged(
                RecommendationSymptomEffect.ShowPrevScreen
            )
        }
    }

}