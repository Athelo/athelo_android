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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ChatEvent, ChatEffect>() {
    private val isGroupConversation: Boolean =
        ChatFragmentArgs.fromSavedStateHandle(savedStateHandle).group
    private val conversationId: Int =
        ChatFragmentArgs.fromSavedStateHandle(savedStateHandle).conversationId

    private var conversation: Conversation? = null

    private var canLoadNextPage: Boolean = true
    private var lastMessageId: Long? = null

    private val allMessages = mutableListOf<ConversationInfo.ConversationMessage>()
    private lateinit var user: User

    private var currentState = ChatViewState(privateChat = !isGroupConversation)

    private val _viewState = MutableStateFlow(currentState)
    val viewState = _viewState.asStateFlow()

    private val pageSize = 100

    private var messageObserveJob: Job? = null

    private var lastVisibleElement: Int? = null

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
                sendChatMessage(event.message)
            }
            is ChatEvent.LastVisibleElement -> lastVisibleElement = event.element
            ChatEvent.SayHelloToEveryone -> sendHelloMessage()
        }
    }

    private fun loadNextPage() {
        launchRequest {
            if (!::user.isInitialized)
                user = loadProfile() ?: throw AuthorizationException()
            val conversation = this.conversation ?: return@launchRequest
            val result =
                loadConversationHistory(
                    conversation.chatRoomId,
                    lastMessageId = lastMessageId,
                    limit = pageSize
                )

            canLoadNextPage = result.size == pageSize

            sendNewMessagesToUi(result, false)
        }
    }

    private fun showLoading() {
        notifyStateChanged(currentState.copy(isLoading = true))
    }

    override fun handleError(throwable: Throwable) {
        hideLoading()
        super.handleError(throwable)
    }

    private fun hideLoading() {
        notifyStateChanged(currentState.copy(isLoading = false))
    }

    private fun notifyStateChanged(newState: ChatViewState) {
        currentState = newState
        launchOnUI { _viewState.emit(currentState) }
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
        val conversation = loadConversation(conversationId, isGroupConversation)
        this.conversation = conversation
        notifyStateChanged(
            currentState.copy(
                conversation = conversation,
                shouldShowHello = preferences.shouldShowHello(conversationId) && conversation.myConversation
            )
        )
        hideLoading()
        startObservingNewMessages(conversation.chatRoomId)
    }

    private fun handleMoreMenuAction(action: ChatMoreButtonClickAction) {
        when (action) {
            ChatMoreButtonClickAction.ShowPopup ->
                notifyStateChanged(currentState.copy(showMorePopup = true))
            ChatMoreButtonClickAction.DismissPopup ->
                notifyStateChanged(currentState.copy(showMorePopup = false))
            ChatMoreButtonClickAction.Leave -> {
                notifyStateChanged(currentState.copy(showMorePopup = false))
                leaveTheConversation()
            }
            ChatMoreButtonClickAction.Mute -> {
                //TODO mute status with server?
                notifyStateChanged(currentState.copy(showMorePopup = false, isMuted = true))
            }
            ChatMoreButtonClickAction.UnMute -> {
                //TODO mute status with server?
                notifyStateChanged(currentState.copy(showMorePopup = false, isMuted = false))
            }
        }
    }

    private fun startObservingNewMessages(chatId: String) {
        if (messageObserveJob != null) return
        messageObserveJob = observeNewChatMessages(chatId).onEach {
            sendNewMessagesToUi(listOf(it), true)
        }.launchIn(viewModelScope)
    }

    private fun sendNewMessagesToUi(
        result: List<ConversationInfo.ConversationMessage>,
        isNewMessage: Boolean
    ) {
        if (result.isNotEmpty())
            if (isNewMessage) allMessages.addAll(0, result) else allMessages.addAll(result)

        lastMessageId = allMessages.mapNotNull { it.messageId.toLongOrNull() }.minOrNull()

        notifyStateChanged(
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
        val chatId = conversation?.chatRoomId ?: return
        if (message.isBlank()) return

        preferences.setShowHello(conversationId, false)
        notifyStateChanged(currentState.copy(shouldShowHello = false))

        sendChatMessage(chatId, message.trim())
    }
}