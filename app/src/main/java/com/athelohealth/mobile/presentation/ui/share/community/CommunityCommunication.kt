package com.athelohealth.mobile.presentation.ui.share.community

import com.athelohealth.mobile.presentation.model.member.User
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

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