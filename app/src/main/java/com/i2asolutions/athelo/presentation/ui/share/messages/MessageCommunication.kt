package com.i2asolutions.athelo.presentation.ui.share.messages

import com.i2asolutions.athelo.presentation.model.chat.PrivateConversation
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

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