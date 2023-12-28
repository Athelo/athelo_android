package com.athelohealth.mobile.presentation.ui.share.chats

import com.athelohealth.mobile.presentation.model.chat.Conversation
import com.athelohealth.mobile.presentation.model.member.User
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

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