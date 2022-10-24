package com.i2asolutions.athelo.presentation.ui.askAthelo

import com.i2asolutions.athelo.presentation.model.application.FAQ
import com.i2asolutions.athelo.presentation.model.askAthelo.AskAtheloSection
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.application.LoadFAQUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AskAtheloViewModel @Inject constructor(private val loadFAQ: LoadFAQUseCase) :
    BaseViewModel<AskAtheloEvent, AskAtheloEffect>() {
    private var currentState = AskAtheloViewState()
    private var questions = listOf<FAQ>()
    private var expandedQuestions: FAQ? = null

    private val _state = MutableStateFlow(currentState)
    val state = _state.asStateFlow()

    override fun loadData() {
        launchRequest {
            questions = loadAllFAQData()
            notifyStateChanged(
                currentState.copy(
                    isLoading = false,
                    questions = generateQuestionList()
                )
            )
        }
    }

    override fun handleEvent(event: AskAtheloEvent) {
        when (event) {
            AskAtheloEvent.BackButtonClick -> notifyEffectChanged(AskAtheloEffect.ShowPrevScreen)
            AskAtheloEvent.SendFeedbackClick -> notifyEffectChanged(AskAtheloEffect.ShowFeedbackScreen)
            is AskAtheloEvent.LinkClicked -> notifyEffectChanged(
                AskAtheloEffect.OpenLinkScreen(
                    event.link
                )
            )
            is AskAtheloEvent.SelectQuestion -> {
                expandedQuestions =
                    if (expandedQuestions == event.question.faq) null else event.question.faq
                notifyStateChanged(currentState.copy(questions = generateQuestionList()))
            }
        }
    }

    private fun generateQuestionList() =
        questions.map { AskAtheloSection(it, expandedQuestions == it) }

    private fun notifyStateChanged(newState: AskAtheloViewState) {
        currentState = newState
        launchOnUI { _state.emit(currentState) }
    }

    private suspend fun loadAllFAQData(nextUrl: String? = null): List<FAQ> {
        val result = loadFAQ(nextUrl)
        return if (result.next.isNullOrBlank()) result.result else result.result + loadAllFAQData(
            result.next
        )
    }
}