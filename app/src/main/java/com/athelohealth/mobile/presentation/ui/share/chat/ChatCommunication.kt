package com.athelohealth.mobile.presentation.ui.share.chat

import com.athelohealth.mobile.presentation.model.chat.ChatMoreButtonClickAction
import com.athelohealth.mobile.presentation.model.chat.Conversation
import com.athelohealth.mobile.presentation.model.chat.ConversationInfo
import com.athelohealth.mobile.presentation.model.member.User
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

data class ChatViewState(
    override val isLoading: Boolean = true,
    val conversation: Conversation? = null,
    val messages: List<ConversationInfo.ConversationMessage> = emptyList(),
    val canLoadNextPage: Boolean = false,
    val currentUser: User = User(),
    val showMorePopup: Boolean = false,
    val isMuted: Boolean = false,
    val shouldScrollToBottom: Boolean = false,
    val shouldShowHello: Boolean = false,
    val privateChat: Boolean = false,
) : BaseViewState

sealed interface ChatEvent : BaseEvent {
    object LoadNextPage : ChatEvent
    object BackClick : ChatEvent
    object JoinConversationClicked : ChatEvent
    object SayHelloToEveryone : ChatEvent
    class SendMessageClicked(val message: String) : ChatEvent
    class LeaveButtonClick(val conversationId: Int) : ChatEvent
    class MoreClick(val action: ChatMoreButtonClickAction) : ChatEvent
    class LastVisibleElement(val element: Int) : ChatEvent
}

sealed interface ChatEffect : BaseEffect {
    class ShowConversation(val conversationId: Int) : ChatEffect
    object ShowMenuScreen : ChatEffect
    object ShowMyProfileScreen : ChatEffect
    object GoBack : ChatEffect
}