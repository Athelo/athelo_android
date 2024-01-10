package com.athelohealth.mobile.presentation.ui.share.chats

import com.athelohealth.mobile.presentation.model.chat.Conversation
import com.athelohealth.mobile.presentation.model.member.User
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.chat.LoadConversationsUseCase
import com.athelohealth.mobile.useCase.member.LoadCachedUserUseCase
import com.athelohealth.mobile.useCase.member.LoadMyProfileUseCase
import com.athelohealth.mobile.utils.AuthorizationException
import com.athelohealth.mobile.utils.PreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val preferenceHelper: PreferenceHelper,
    private val loadConversations: LoadConversationsUseCase,
    private val loadProfile: LoadMyProfileUseCase,
    private val loadCachedProfile: LoadCachedUserUseCase,
) : BaseViewModel<ChatListEvent, ChatListEffect, ChatListViewState>(ChatListViewState(showLanding = false)) {
    private var nextUrl: String? = null
    private val allConversation = mutableListOf<Conversation>()
    private lateinit var user: User

    init {
        notifyStateChange(ChatListViewState(showLanding = showLanding))
    }

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

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
                notifyStateChange(currentState.copy(showLanding = preferenceHelper.showChatGroupLanding))
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
            notifyStateChange(
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
        notifyStateChange(currentState.copy(isLoading = true))
    }

}