package com.i2asolutions.athelo.presentation.ui.share.feedback

import com.i2asolutions.athelo.presentation.model.base.InputType
import com.i2asolutions.athelo.presentation.model.enums.EnumItem
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class FeedbackViewState(
    override val isLoading: Boolean = true,
    val enableSendButton: Boolean = false,
    val selectedOption: EnumItem = EnumItem.EMPTY,
    val options: List<EnumItem> = emptyList(),
    val message: String = "",
) : BaseViewState

sealed interface FeedbackEvent : BaseEvent {
    data class InputValueChanged(val inputType: InputType) : FeedbackEvent
    object BackButtonClick : FeedbackEvent
    object SendButtonClick : FeedbackEvent
}

sealed interface FeedbackEffect : BaseEffect {
    object ShowPrevScreen : FeedbackEffect
}