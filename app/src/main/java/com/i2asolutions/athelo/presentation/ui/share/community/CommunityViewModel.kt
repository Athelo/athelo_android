package com.i2asolutions.athelo.presentation.ui.share.community

import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.member.LoadMyProfileUseCase
import com.i2asolutions.athelo.utils.AuthorizationException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val loadProfile: LoadMyProfileUseCase,
) : BaseViewModel<CommunityEvent, CommunityEffect>() {

    private var currentState = CommunityViewState()

    private val _viewState = MutableStateFlow(currentState)
    val viewState = _viewState.asStateFlow()

    override fun loadData() {
        launchRequest {
            notifyStateChanged(
                currentState.copy(
                    isLoading = false,
                    currentUser = loadProfile() ?: throw AuthorizationException()
                )
            )
        }
    }

    override fun handleError(throwable: Throwable) {
        notifyStateChanged(currentState.copy(isLoading = false))
        super.handleError(throwable)
    }

    override fun handleEvent(event: CommunityEvent) {
        when (event) {
            CommunityEvent.MenuClick -> notifyEffectChanged(CommunityEffect.ShowMenuScreen)
            CommunityEvent.ChatListClick -> notifyEffectChanged(CommunityEffect.ShowChatsScreen)
            CommunityEvent.MyProfileClick -> notifyEffectChanged(CommunityEffect.ShowMyProfileScreen)
        }
    }

    private fun notifyStateChanged(newState: CommunityViewState) {
        currentState = newState
        launchOnUI { _viewState.emit(currentState) }
    }
}