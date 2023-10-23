package com.i2asolutions.athelo.presentation.ui.share.tutorial

import com.i2asolutions.athelo.presentation.model.tutorial.TutorialPageItem
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class TutorialViewState(
    val pages: List<TutorialPageItem>,
    override val isLoading: Boolean = false
) : BaseViewState

sealed interface TutorialEffect : BaseEffect {
    object CloseApp : TutorialEffect
    object ShowAuthorization : TutorialEffect
}

sealed interface TutorialEvent : BaseEvent {
    class UpdateScreen(val currentPage: TutorialPageItem) : TutorialEvent
}