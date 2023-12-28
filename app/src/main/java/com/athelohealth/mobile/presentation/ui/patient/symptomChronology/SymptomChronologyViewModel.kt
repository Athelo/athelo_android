package com.athelohealth.mobile.presentation.ui.patient.symptomChronology

import com.athelohealth.mobile.presentation.model.health.SymptomChronology
import com.athelohealth.mobile.presentation.model.health.SymptomChronologyHeader
import com.athelohealth.mobile.presentation.model.health.SymptomChronologyItem
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.health.LoadSymptomsChronologyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SymptomChronologyViewModel @Inject constructor(
    private val loadChronology: LoadSymptomsChronologyUseCase,
) : BaseViewModel<SymptomChronologyEvent, SymptomChronologyEffect>() {
    private var nextUrl: String? = null
    private val allData: MutableSet<SymptomChronology> = mutableSetOf()
    private var currentState = SymptomChronologyViewState()

    private val _state = MutableStateFlow(currentState)
    val state = _state.asStateFlow()

    override fun loadData() {
        launchRequest { loadFirstPage() }
    }

    override fun handleEvent(event: SymptomChronologyEvent) {
        when (event) {
            SymptomChronologyEvent.BackButtonClick -> notifyEffectChanged(SymptomChronologyEffect.ShowPrevScreen)
            SymptomChronologyEvent.LoadFirstPage -> launchRequest {
                loadFirstPage()
            }
            SymptomChronologyEvent.LoadNextPage -> launchRequest {
                loadNextPage()
            }
        }
    }

    private suspend fun loadFirstPage() {
        nextUrl = null
        allData.clear()
        loadNextPage()
    }

    private suspend fun loadNextPage() {
        notifyStateChanged(
            currentState.copy(
                isLoading = true
            )
        )
        val response = loadChronology(nextUrl)
        nextUrl = response.next
        val items = response.result
        if (items.isNotEmpty()) {
            allData.addAll(items)
        }
        notifyStateChanged(
            currentState.copy(
                isLoading = false,
                items = generateList(),
                canLoadMore = !nextUrl.isNullOrBlank()
            )
        )
    }

    private fun generateList(): List<SymptomChronologyItem> {
        val tmpAllList = allData.asSequence().sortedByDescending { it.date }.toList()
        val groupedItems = tmpAllList.groupBy { it.yearHeader }
        return buildList {
            groupedItems.keys.sortedByDescending { it }.forEach {
                add(SymptomChronologyHeader(it))
                val items = groupedItems[it]
                if (!items.isNullOrEmpty())
                    addAll(items)
            }
        }
    }

    private fun notifyStateChanged(newState: SymptomChronologyViewState) {
        currentState = newState
        launchOnUI { _state.emit(currentState) }
    }
}