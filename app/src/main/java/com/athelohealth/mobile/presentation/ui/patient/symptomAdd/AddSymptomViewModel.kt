package com.athelohealth.mobile.presentation.ui.patient.symptomAdd

import androidx.lifecycle.SavedStateHandle
import com.athelohealth.mobile.presentation.model.base.InputType
import com.athelohealth.mobile.presentation.model.health.Symptom
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.health.AddSymptomUseCase
import com.athelohealth.mobile.useCase.health.LoadMySymptomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddSymptomViewModel @Inject constructor(
    private val saveSymptom: AddSymptomUseCase,
    private val loadSymptom: LoadMySymptomUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<AddSymptomEvent, AddSymptomEffect, AddSymptomViewState>(AddSymptomViewState(isLoading = false, title = "")) {
    private val symptom: Symptom =
        AddSymptomDialogFragmentArgs.fromSavedStateHandle(savedStateHandle).symptom
    private val date = AddSymptomDialogFragmentArgs.fromSavedStateHandle(savedStateHandle).date
    private var comment = ""
    init {
        notifyStateChange(AddSymptomViewState(isLoading = false, title = symptom.name))
    }


    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }
    override fun loadData() {}

    override fun handleEvent(event: AddSymptomEvent) {
        when (event) {
            AddSymptomEvent.CloseButtonClick -> notifyEffectChanged(
                AddSymptomEffect.ShowPrevScreen()
            )
            AddSymptomEvent.SaveButtonClick -> selfBlockRun {
                launchRequest {
                    notifyStateChange(currentState.copy(isLoading = true))
                    val symptom = saveSymptom(symptom, comment, date)
                    val fullSymptom = loadSymptom(symptom.id)
                    notifyEffectChanged(AddSymptomEffect.ShowPrevScreen(fullSymptom))
                }
            }
            is AddSymptomEvent.InputValueChanged -> {
                when (event.inputType) {
                    is InputType.Text -> {
                        comment = event.inputType.value
                    }
                    else -> {/*Ignore other types */ }
                }
                notifyStateChange(currentState.copy())
            }
        }
    }

}