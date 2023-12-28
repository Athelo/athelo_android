package com.athelohealth.mobile.presentation.ui.share.feedback

import com.athelohealth.mobile.presentation.model.base.InputType
import com.athelohealth.mobile.presentation.model.enums.EnumItem
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

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