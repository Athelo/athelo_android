package com.i2asolutions.athelo.presentation.ui.patient.wellbeing

import com.i2asolutions.athelo.extensions.normalizeValue
import com.i2asolutions.athelo.presentation.model.base.InputType
import com.i2asolutions.athelo.presentation.model.calendar.Day
import com.i2asolutions.athelo.presentation.model.enums.EnumItem
import com.i2asolutions.athelo.presentation.model.health.Symptom
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.health.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class WellbeingViewModel @Inject constructor(
    private val loadWellbeing: LoadWellbeingForDayUseCase,
    private val saveWellbeing: SaveWellbeingForDayUseCase,
    private val loadMySymptoms: LoadMySymptomsUseCase,
    private val loadSymptoms: LoadSymptomsUseCase,
    private val removeSymptom: RemoveSymptomUseCase,
) : BaseViewModel<WellbeingEvent, WellbeingEffect>() {
    private val allSymptoms: MutableSet<Symptom> = mutableSetOf()

    private var currentState = WellbeingViewState()

    private val _state = MutableStateFlow(currentState)
    val state = _state.asStateFlow()
    var nextUrl: String? = null
    var newSymptomSelected: Symptom? = null

    override fun handleError(throwable: Throwable) {
        notifyStateChanged(currentState.copy(isLoading = false))
        super.handleError(throwable)
    }

    override fun loadData() {
        showLoading()
        launchRequest {
            loadAllSymptomsIfNeeded()
            val selectedDay = currentState.selectedDay
            val mySymptoms = loadAllMySymptoms(selectedDay)
            val response = loadWellbeing(nextUrl, selectedDay)
            nextUrl = response.next
            val currentFeeling =
                response.result.asSequence().sortedBy { it.id }
                    .lastOrNull()?.feeling?.normalizeValue()
            notifyStateChanged(
                currentState.copy(
                    isLoading = false,
                    currentFeeling = currentFeeling ?: 1,
                    initialFeeling = currentFeeling,
                    feelingButtonEnabled = false,
                    allSymptoms = allSymptoms.map { it.toEnumItem() }
                        .sortedBy { it.label.lowercase() },
                    currentSymptoms = mySymptoms,
                )
            )
        }
    }

    private suspend fun loadAllSymptomsIfNeeded() {
        if (allSymptoms.isEmpty()) {
            var nextUrl: String? = null
            var isFirstRun = true
            while (isFirstRun || !nextUrl.isNullOrBlank()) {
                if (isFirstRun) {
                    isFirstRun = false
                    allSymptoms.clear()
                }
                val res = loadSymptoms(nextUrl)
                nextUrl = res.next
                allSymptoms.addAll(res.result)
            }
        }
    }

    private suspend fun loadAllMySymptoms(day: Day): List<Symptom> {
        val symptoms = mutableListOf<Symptom>()
        var nextUrl: String? = null
        var isFirstRun = true
        while (isFirstRun || !nextUrl.isNullOrBlank()) {
            if (isFirstRun) {
                isFirstRun = false
            }
            val res = loadMySymptoms(nextUrl, day)
            nextUrl = res.next
            symptoms.addAll(res.result)
        }
        return symptoms
    }

    override fun handleEvent(event: WellbeingEvent) {
        when (event) {
            is WellbeingEvent.DayValueChanged -> {
                notifyStateChanged(currentState.copy(isLoading = true, selectedDay = event.day))
                launchRequest {
                    nextUrl = null
                    loadData()
                }
            }
            is WellbeingEvent.FeelingValueChanged -> {
                notifyStateChanged(
                    currentState.copy(
                        currentFeeling = event.feeling,
                        feelingButtonEnabled = event.feeling != currentState.initialFeeling
                    )
                )
            }
            WellbeingEvent.EditSymptomInformationClick -> {

            }
            is WellbeingEvent.InputValueChanged -> handleInputValue(event.inputType)
            WellbeingEvent.AddSymptomClick -> {
                newSymptomSelected?.let { symptom ->
                    notifyEffectChanged(
                        WellbeingEffect.ShowAddSymptomScreen(
                            symptom,
                            currentState.selectedDay
                        )
                    )
                }
                    ?: errorMessage("No symptom selected. Please select one before going to next step.")
            }
            WellbeingEvent.SaveMyFeelingClick -> saveFeeling()
            WellbeingEvent.SymptomChronologyClick -> {}
            WellbeingEvent.BackButtonClick -> notifyEffectChanged(WellbeingEffect.ShowPrevScreen)
            is WellbeingEvent.SymptomAdded -> {
                newSymptomSelected = null
                notifyStateChanged(
                    currentState.copy(
                        selectedSymptom = EnumItem.EMPTY,
                        currentSymptoms = currentState.currentSymptoms.toMutableList()
                            .also { it.add(event.symptom) }.toList()
                    )
                )
            }
            is WellbeingEvent.SymptomRemovedClick -> {
                notifyStateChanged(currentState.copy(removeConfirmPopup = event.symptomId))
            }
            is WellbeingEvent.SymptomRemovedCancelClick -> {
                notifyStateChanged(currentState.copy(removeConfirmPopup = null))
            }
            is WellbeingEvent.SymptomRemovedConfirmClick -> launchRequest {
                showLoading()
                if (removeSymptom(event.symptomId))
                    notifyStateChanged(
                        currentState.copy(
                            isLoading = false,
                            currentSymptoms = currentState.currentSymptoms.toMutableList()
                                .also { list -> list.removeIf { it.id == event.symptomId } }
                                .toList(),
                            removeConfirmPopup = null
                        )
                    )
                else {
                    notifyStateChanged(
                        currentState.copy(
                            isLoading = false,
                            removeConfirmPopup = null
                        )
                    )
                }
            }
            is WellbeingEvent.SymptomClick -> notifyEffectChanged(
                WellbeingEffect.ShowInfoSymptomScreen(
                    event.symptom
                )
            )
        }
    }

    private fun handleInputValue(inputType: InputType) {
        when (inputType) {
            is InputType.DropDown -> {
                newSymptomSelected =
                    allSymptoms.firstOrNull { it.id == inputType.value.toInt() }
                notifyStateChanged(
                    currentState.copy(
                        selectedSymptom = newSymptomSelected?.toEnumItem() ?: EnumItem.EMPTY
                    )
                )
            }
            else -> {/*Ignore other types*/
            }
        }
    }

    private fun saveFeeling() {
        showLoading()
        launchRequest {
            val result = saveWellbeing(
                currentState.selectedDay,
                currentState.currentFeeling.normalizeValue(),
            )
            notifyStateChanged(
                currentState.copy(
                    isLoading = false,
                    initialFeeling = result.feeling.normalizeValue(),
                    currentFeeling = result.feeling.normalizeValue(),
                    feelingButtonEnabled = false
                )
            )
        }
    }

    private fun showLoading() {
        notifyStateChanged(currentState.copy(isLoading = true))
    }

    private fun notifyStateChanged(newState: WellbeingViewState) {
        currentState = newState
        launchOnUI { _state.emit(currentState) }
    }
}