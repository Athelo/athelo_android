package com.i2asolutions.athelo.presentation.ui.chats

import com.i2asolutions.athelo.presentation.model.chat.Conversation
import com.i2asolutions.athelo.presentation.model.member.User
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class ChatListViewState(
    override val isLoading: Boolean = true,
    val showLanding: Boolean = true,
    val yourConversation: List<Conversation> = emptyList(),
    val conversations: List<Conversation> = emptyList(),
    val canLoadNextPage: Boolean = false,
    val currentUser: User = User(),
): BaseViewState

sealed interface ChatListEvent: BaseEvent {
    object MenuClick : ChatListEvent
    object LetStartClick : ChatListEvent
    object LoadNextPage : ChatListEvent
    object RefreshData : ChatListEvent
    object MyProfileClick : ChatListEvent
    class ConversationClick(val conversationId: Int) : ChatListEvent
}

sealed interface ChatListEffect: BaseEffect {
    class ShowConversation(val conversationId: Int) : ChatListEffect
    object ShowMenuScreen : ChatListEffect
    object ShowMyProfileScreen : ChatListEffect
}