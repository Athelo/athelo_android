package com.i2asolutions.athelo.presentation.ui.community

import com.i2asolutions.athelo.presentation.model.member.User
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class CommunityViewState(
    override val isLoading: Boolean = true,
    val currentUser: User = User(),
) : BaseViewState

sealed interface CommunityEvent : BaseEvent {
    object MenuClick : CommunityEvent
    object MyProfileClick : CommunityEvent
    object ChatListClick : CommunityEvent
}

sealed interface CommunityEffect : BaseEffect {
    object ShowMenuScreen : CommunityEffect
    object ShowMyProfileScreen : CommunityEffect
    object ShowChatsScreen : CommunityEffect
}