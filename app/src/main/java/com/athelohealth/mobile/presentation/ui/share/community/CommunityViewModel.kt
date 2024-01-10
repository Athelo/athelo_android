package com.athelohealth.mobile.presentation.ui.share.community

import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.member.LoadMyProfileUseCase
import com.athelohealth.mobile.utils.AuthorizationException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val loadProfile: LoadMyProfileUseCase,
) : BaseViewModel<CommunityEvent, CommunityEffect, CommunityViewState>(CommunityViewState()) {

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    override fun loadData() {
        launchRequest {
            notifyStateChange(
                currentState.copy(
                    isLoading = false,
                    currentUser = loadProfile() ?: throw AuthorizationException()
                )
            )
        }
    }

    override fun handleEvent(event: CommunityEvent) {
        when (event) {
            CommunityEvent.MenuClick -> notifyEffectChanged(CommunityEffect.ShowMenuScreen)
            CommunityEvent.ChatListClick -> notifyEffectChanged(CommunityEffect.ShowChatsScreen)
            CommunityEvent.MyProfileClick -> notifyEffectChanged(CommunityEffect.ShowMyProfileScreen)
        }
    }
}