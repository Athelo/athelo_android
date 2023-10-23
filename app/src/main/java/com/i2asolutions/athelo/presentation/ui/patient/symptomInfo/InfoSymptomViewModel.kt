package com.i2asolutions.athelo.presentation.ui.patient.symptomInfo

import androidx.lifecycle.SavedStateHandle
import com.i2asolutions.athelo.presentation.model.health.Symptom
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class InfoSymptomViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel<InfoSymptomEvent, InfoSymptomEffect>() {
    private val symptom: Symptom =
        InfoSymptomDialogFragmentArgs.fromSavedStateHandle(savedStateHandle).symptom
    private var currentState =
        InfoSymptomViewState(false, title = symptom.name, comment = symptom.comment ?: "")

    private val _state = MutableStateFlow(currentState)
    val state = _state.asStateFlow()

    override fun loadData() {

    }

    override fun handleEvent(event: InfoSymptomEvent) {
        when (event) {
            InfoSymptomEvent.BackButtonClick -> notifyEffectChanged(InfoSymptomEffect.ShowPrevScreen)
            InfoSymptomEvent.RecommendationButtonClick -> notifyEffectChanged(
                InfoSymptomEffect.ShowRecommendationScreen(
                    symptom.symptomId
                )
            )
        }
    }

    private fun notifyStateChanged(newState: InfoSymptomViewState) {
        currentState = newState
        launchOnUI { _state.emit(currentState) }
    }
}