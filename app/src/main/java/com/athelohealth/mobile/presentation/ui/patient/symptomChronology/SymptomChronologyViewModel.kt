package com.athelohealth.mobile.presentation.ui.patient.symptomChronology

import com.athelohealth.mobile.presentation.model.health.SymptomChronology
import com.athelohealth.mobile.presentation.model.health.SymptomChronologyHeader
import com.athelohealth.mobile.presentation.model.health.SymptomChronologyItem
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.health.LoadSymptomsChronologyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SymptomChronologyViewModel @Inject constructor(
    private val loadChronology: LoadSymptomsChronologyUseCase,
) : BaseViewModel<SymptomChronologyEvent, SymptomChronologyEffect, SymptomChronologyViewState>(SymptomChronologyViewState()) {
    private var nextUrl: String? = null
    private val allData: MutableSet<SymptomChronology> = mutableSetOf()

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

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
        notifyStateChange(
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
        notifyStateChange(
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

}