package com.athelohealth.mobile.presentation.ui.share.messages

import com.athelohealth.mobile.presentation.model.chat.PrivateConversation
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

data class MessageViewState(
    override val isLoading: Boolean = false,
    val messages: List<PrivateConversation> = emptyList()
) : BaseViewState

sealed interface MessageEvent : BaseEvent {
    object BackButtonClick : MessageEvent
    class ConversationItemClick(val conversation: PrivateConversation) : MessageEvent
}

sealed interface MessageEffect : BaseEffect {
    object ShowPrevScreen : MessageEffect
    class ShowChatScreen(val conversation: PrivateConversation) : MessageEffect
}