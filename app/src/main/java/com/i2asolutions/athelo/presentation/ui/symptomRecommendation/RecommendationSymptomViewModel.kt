package com.i2asolutions.athelo.presentation.ui.symptomRecommendation

import androidx.lifecycle.SavedStateHandle
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.health.LoadSymptomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RecommendationSymptomViewModel @Inject constructor(
    private val loadSymptom: LoadSymptomUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<RecommendationSymptomEvent, RecommendationSymptomEffect>() {
    private val symptomId: Int =
        RecommendationSymptomFragmentArgs.fromSavedStateHandle(savedStateHandle).symptomId
    private var currentState = RecommendationSymptomViewState()

    private val _state = MutableStateFlow(currentState)
    val state = _state.asStateFlow()

    override fun loadData() {
        notifyStateChanged(currentState.copy(isLoading = true))
        launchRequest {
            val symptom = loadSymptom(symptomId = symptomId)
            notifyStateChanged(
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

    private fun notifyStateChanged(newState: RecommendationSymptomViewState) {
        currentState = newState
        launchOnUI { _state.emit(currentState) }
    }
}