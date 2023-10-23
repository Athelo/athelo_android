package com.i2asolutions.athelo.presentation.ui.share.askAthelo

import com.i2asolutions.athelo.presentation.model.askAthelo.AskAtheloSection
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState


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