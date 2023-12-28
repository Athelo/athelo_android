package com.athelohealth.mobile.presentation.ui.share.tutorial

import com.athelohealth.mobile.presentation.model.tutorial.TutorialPageItem
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

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