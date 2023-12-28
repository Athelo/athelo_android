package com.athelohealth.mobile.presentation.ui.share.askAthelo

import com.athelohealth.mobile.presentation.model.askAthelo.AskAtheloSection
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState


data class AskAtheloViewState(
    override val isLoading: Boolean = true,
    val questions: List<AskAtheloSection> = emptyList()
) : BaseViewState

sealed interface AskAtheloEvent : BaseEvent {
    data class LinkClicked(val link: String) : AskAtheloEvent
    class SelectQuestion(val question: AskAtheloSection) : AskAtheloEvent

    object SendFeedbackClick : AskAtheloEvent
    object BackButtonClick : AskAtheloEvent

}

sealed interface AskAtheloEffect : BaseEffect {
    class OpenLinkScreen(val url: String) : AskAtheloEffect
    object ShowPrevScreen : AskAtheloEffect
    object ShowFeedbackScreen : AskAtheloEffect
}