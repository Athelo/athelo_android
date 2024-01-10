package com.athelohealth.mobile.presentation.ui.share.askAthelo

import com.athelohealth.mobile.presentation.model.application.FAQ
import com.athelohealth.mobile.presentation.model.askAthelo.AskAtheloSection
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.application.LoadFAQUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AskAtheloViewModel @Inject constructor(private val loadFAQ: LoadFAQUseCase) :
    BaseViewModel<AskAtheloEvent, AskAtheloEffect, AskAtheloViewState>(AskAtheloViewState()) {
    private var questions = listOf<FAQ>()
    private var expandedQuestions: FAQ? = null

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    override fun loadData() {
        launchRequest {
            questions = loadAllFAQData()
            notifyStateChange(
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
                notifyStateChange(currentState.copy(questions = generateQuestionList()))
            }
        }
    }

    private fun generateQuestionList() =
        questions.map { AskAtheloSection(it, expandedQuestions == it) }

    private suspend fun loadAllFAQData(nextUrl: String? = null): List<FAQ> {
        val result = loadFAQ(nextUrl)
        return if (result.next.isNullOrBlank()) result.result else result.result + loadAllFAQData(
            result.next
        )
    }
}