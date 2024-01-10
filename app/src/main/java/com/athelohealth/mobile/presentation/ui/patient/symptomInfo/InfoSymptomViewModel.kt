package com.athelohealth.mobile.presentation.ui.patient.symptomInfo

import androidx.lifecycle.SavedStateHandle
import com.athelohealth.mobile.presentation.model.health.Symptom
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InfoSymptomViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel<InfoSymptomEvent, InfoSymptomEffect, InfoSymptomViewState>(InfoSymptomViewState(false, title = "", comment = "")) {
    private val symptom: Symptom =
        InfoSymptomDialogFragmentArgs.fromSavedStateHandle(savedStateHandle).symptom

    init {
        notifyStateChange(InfoSymptomViewState(false, title = symptom.name, comment = symptom.comment ?: ""))
    }

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    override fun loadData() {}

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
}