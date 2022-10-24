package com.i2asolutions.athelo.presentation.ui.symptomAdd

import androidx.lifecycle.SavedStateHandle
import com.i2asolutions.athelo.presentation.model.base.InputType
import com.i2asolutions.athelo.presentation.model.health.Symptom
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.health.AddSymptomUseCase
import com.i2asolutions.athelo.useCase.health.LoadMySymptomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AddSymptomViewModel @Inject constructor(
    private val saveSymptom: AddSymptomUseCase,
    private val loadSymptom: LoadMySymptomUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<AddSymptomEvent, AddSymptomEffect>() {
    private val symptom: Symptom =
        AddSymptomDialogFragmentArgs.fromSavedStateHandle(savedStateHandle).symptom
    private val date = AddSymptomDialogFragmentArgs.fromSavedStateHandle(savedStateHandle).date
    private var comment = ""
    private var currentState = AddSymptomViewState(isLoading = false, title = symptom.name)

    private val _state = MutableStateFlow(currentState)
    val state = _state.asStateFlow()

    override fun loadData() {

    }

    override fun handleError(throwable: Throwable) {
        notifyStateChanged(currentState.copy(isLoading = false))
        super.handleError(throwable)
    }

    override fun handleEvent(event: AddSymptomEvent) {
        when (event) {
            AddSymptomEvent.CloseButtonClick -> notifyEffectChanged(AddSymptomEffect.ShowPrevScreen())
            AddSymptomEvent.SaveButtonClick -> selfBlockRun {
                launchRequest {
                    notifyStateChanged(currentState.copy(isLoading = true))
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
                notifyStateChanged(currentState.copy())
            }
        }
    }

    private fun notifyStateChanged(newState: AddSymptomViewState) {
        currentState = newState
        launchOnUI { _state.emit(currentState) }
    }
}