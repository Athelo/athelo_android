package com.i2asolutions.athelo.presentation.ui.share.chats

import com.i2asolutions.athelo.presentation.model.chat.Conversation
import com.i2asolutions.athelo.presentation.model.member.User
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.chat.LoadConversationsUseCase
import com.i2asolutions.athelo.useCase.member.LoadCachedUserUseCase
import com.i2asolutions.athelo.useCase.member.LoadMyProfileUseCase
import com.i2asolutions.athelo.utils.AuthorizationException
import com.i2asolutions.athelo.utils.PreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val preferenceHelper: PreferenceHelper,
    private val loadConversations: LoadConversationsUseCase,
    private val loadProfile: LoadMyProfileUseCase,
    private val loadCachedProfile: LoadCachedUserUseCase,
) : BaseViewModel<ChatListEvent, ChatListEffect>() {
    private var nextUrl: String? = null
    private val allConversation = mutableListOf<Conversation>()
    private lateinit var user: User

    private var currentState =
        ChatListViewState(showLanding = showLanding)

    private val _viewState = MutableStateFlow(currentState)
    val viewState = _viewState.asStateFlow()

    private val showLanding: Boolean
        get() = preferenceHelper.showChatGroupLanding

    override fun loadData() {
        showLoading()
        nextUrl = null
        allConversation.clear()
        loadNextPage()
    }

    override fun handleEvent(event: ChatListEvent) {
        when (event) {
            is ChatListEvent.ConversationClick -> notifyEffectChanged(
                ChatListEffect.ShowConversation(
                    event.conversationId
                )
            )
            ChatListEvent.LetStartClick -> {
                preferenceHelper.showChatGroupLanding = false
                notifyStateChanged(currentState.copy(showLanding = preferenceHelper.showChatGroupLanding))
            }
            ChatListEvent.MyProfileClick -> notifyEffectChanged(ChatListEffect.ShowMyProfileScreen)
            ChatListEvent.LoadNextPage -> loadNextPage()
            ChatListEvent.MenuClick -> notifyEffectChanged(ChatListEffect.ShowMenuScreen)
            ChatListEvent.RefreshData -> loadData()
        }
    }

    private fun loadNextPage() {
        launchRequest {
            if (!::user.isInitialized)
                user = loadCachedProfile() ?: loadProfile()
                        ?: throw AuthorizationException()
            val result = loadConversations(nextUrl)
            nextUrl = result.next
            if (result.result.isNotEmpty())
                allConversation.addAll(result.result)
            notifyStateChanged(
                currentState.copy(
                    isLoading = false,
                    currentUser = user,
                    canLoadNextPage = !nextUrl.isNullOrBlank(),
                    yourConversation = allConversation.filter { it.myConversation },
                    conversations = allConversation.filter { !it.myConversation },
                    showLanding = showLanding,
                ),
            )
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

    private fun notifyStateChanged(newState: ChatListViewState) {
        currentState = newState
        launchOnUI { _viewState.emit(currentState) }
    }
}