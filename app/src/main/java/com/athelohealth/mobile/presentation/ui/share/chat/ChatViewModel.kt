package com.athelohealth.mobile.presentation.ui.share.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.athelohealth.mobile.presentation.model.chat.ChatMoreButtonClickAction
import com.athelohealth.mobile.presentation.model.chat.Conversation
import com.athelohealth.mobile.presentation.model.chat.ConversationInfo
import com.athelohealth.mobile.presentation.model.member.User
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.chat.*
import com.athelohealth.mobile.useCase.member.LoadMyProfileUseCase
import com.athelohealth.mobile.utils.AuthorizationException
import com.athelohealth.mobile.utils.PreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val loadConversation: LoadConversationDetailUseCase,
    private val joinConversationUseCase: JoinConversationUseCase,
    private val leaveConversationUseCase: LeaveConversationUseCase,
    private val loadProfile: LoadMyProfileUseCase,
    private val loadConversationHistory: LoadConversationHistoryUseCase,
    private val observeNewChatMessages: ObserveNewChatMessagesUseCase,
    private val sendChatMessage: SendChatMessageUseCase,
    private val preferences: PreferenceHelper,
    private val getChatMessagesUseCase: GetChatMessagesUseCase,
    private val postChatMessagesUseCase: PostChatMessagesUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ChatEvent, ChatEffect, ChatViewState>(ChatViewState(privateChat = true)) {
    private val isGroupConversation: Boolean =
        ChatFragmentArgs.fromSavedStateHandle(savedStateHandle).group
    private val conversationId: Int =
        ChatFragmentArgs.fromSavedStateHandle(savedStateHandle).conversationId

    private var conversation: Conversation? = null

    private var canLoadNextPage: Boolean = false
    private var lastMessageId: Long? = null

    private val allMessages = mutableListOf<ConversationInfo.ConversationMessage>()
    private lateinit var user: User

    init {
        notifyStateChange(ChatViewState(privateChat = !isGroupConversation))
        preferences.setShowHello(conversationId, false)
        notifyStateChange(currentState.copy(shouldShowHello = false))
    }

    private val pageSize = 100

    private var messageObserveJob: Job? = null

    private var lastVisibleElement: Int? = null
    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    override fun loadData() {
        launchRequest {
            lastMessageId = null
            showLoading()
            loadConversationDetails()
            loadNextPage()
        }
    }

    override fun handleEvent(event: ChatEvent) {
        when (event) {
            ChatEvent.LoadNextPage -> loadNextPage()
            is ChatEvent.LeaveButtonClick -> launchRequest {
                showLoading()
                leaveConversationUseCase(event.conversationId)
            }
            ChatEvent.BackClick -> notifyEffectChanged(ChatEffect.GoBack)
            ChatEvent.JoinConversationClicked -> joinTheConversation()
            is ChatEvent.MoreClick -> handleMoreMenuAction(event.action)
            is ChatEvent.SendMessageClicked -> {
                sendChatMessage(event.message.trim())
            }
            is ChatEvent.LastVisibleElement -> lastVisibleElement = event.element
            ChatEvent.SayHelloToEveryone -> sendHelloMessage()
        }
    }

    private fun loadNextPage() {
        launchRequest {
            while (true) {
                if (currentState.isLoading) {
                    delay(5000)
                    continue
                }
                val chatMessages = getChatMessagesUseCase(conversationId)
                sendNewMessagesToUi(chatMessages, false)
                delay(5000)
            }
//            val conversation = this.conversation ?: return@launchRequest
//            val result =
//                loadConversationHistory(
//                    conversation.chatRoomId,
//                    lastMessageId = lastMessageId,
//                    limit = pageSize
//                )
//
//            canLoadNextPage = result.size == pageSize
//            sendNewMessagesToUi(result, false)
        }
    }

    private fun showLoading() {
        notifyStateChange(currentState.copy(isLoading = true))
    }

    private fun joinTheConversation() {
        launchRequest {
            showLoading()
            val success = joinConversationUseCase(conversationId)
            if (!success) {
                handleError(Exception("Cannot join this conversation."))
                return@launchRequest
            }
            loadConversationDetails()
        }
    }

    private fun leaveTheConversation() {
        launchRequest {
            showLoading()
            val success = leaveConversationUseCase(conversationId)
            if (!success) {
                handleError(Exception("Cannot leave this conversation."))
                return@launchRequest
            }
            loadConversationDetails()
        }
    }

    private suspend fun loadConversationDetails() {
        if (!::user.isInitialized)
            user = loadProfile() ?: throw AuthorizationException()
        val conversation = loadConversation(conversationId, isGroupConversation)
        this.conversation = conversation
        notifyStateChange(
            currentState.copy(
                conversation = conversation,
                shouldShowHello = preferences.shouldShowHello(conversationId) && conversation.myConversation
            )
        )
        pauseLoadingState()
        startObservingNewMessages(conversation.chatRoomId)
    }

    private fun handleMoreMenuAction(action: ChatMoreButtonClickAction) {
        when (action) {
            ChatMoreButtonClickAction.ShowPopup ->
                notifyStateChange(currentState.copy(showMorePopup = true))
            ChatMoreButtonClickAction.DismissPopup ->
                notifyStateChange(currentState.copy(showMorePopup = false))
            ChatMoreButtonClickAction.Leave -> {
                notifyStateChange(currentState.copy(showMorePopup = false))
                leaveTheConversation()
            }
            ChatMoreButtonClickAction.Mute -> {
                //TODO mute status with server?
                notifyStateChange(currentState.copy(showMorePopup = false, isMuted = true))
            }
            ChatMoreButtonClickAction.UnMute -> {
                //TODO mute status with server?
                notifyStateChange(currentState.copy(showMorePopup = false, isMuted = false))
            }
        }
    }

    private fun startObservingNewMessages(chatId: String) {
        if (messageObserveJob != null) return
        messageObserveJob = observeNewChatMessages(chatId).onEach {
//            sendNewMessagesToUi(listOf(it), true)
        }.launchIn(viewModelScope)
    }

    private fun sendNewMessagesToUi(
        result: List<ConversationInfo.ConversationMessage>,
        isNewMessage: Boolean
    ) {
        if (result.isNotEmpty())
            if (isNewMessage) allMessages.addAll(0, result) else allMessages.addAll(result)

        lastMessageId = allMessages.mapNotNull { it.messageId.toLongOrNull() }.minOrNull()

        notifyStateChange(
            currentState.copy(
                isLoading = false,
                currentUser = user,
                canLoadNextPage = canLoadNextPage,
                messages = ArrayList(allMessages.distinct()),
                shouldScrollToBottom = lastVisibleElement == 0
            ),
        )
    }

    private fun sendHelloMessage() {
        sendChatMessage("Hello everyone!")
    }

    private fun sendChatMessage(message: String) {
        launchRequest {
            if (message.isEmpty()) return@launchRequest
            notifyStateChange(currentState.copy(isLoading = true))
            val chatMessage = postChatMessagesUseCase(conversationId, message)
            sendNewMessagesToUi(listOf(chatMessage), true)
        }
//        val chatId = conversation?.chatRoomId ?: return
//        if (message.isBlank()) return
//
//        preferences.setShowHello(conversationId, false)
//        notifyStateChange(currentState.copy(shouldShowHello = false))
//
//        sendChatMessage(chatId, message.trim())
    }
}