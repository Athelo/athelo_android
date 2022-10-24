package com.i2asolutions.athelo.presentation.ui.home

import com.i2asolutions.athelo.presentation.model.home.HomeItems
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class HomeViewState(
    override val isLoading: Boolean,
    val displayName: String,
    val userAvatar: String? = null,
    val listItems: List<HomeItems>
) : BaseViewState

sealed interface HomeEvent : BaseEvent {
    object MenuClick : HomeEvent
    object MyProfileClick : HomeEvent
    class ItemClick(val item: HomeItems) : HomeEvent
}

sealed interface HomeEffect : BaseEffect {
    object ShowMenuScreen : HomeEffect
    object ShowMyProfileScreen : HomeEffect
    object ShowTrackWellbeingScreen : HomeEffect
    object ShowChatScreen : HomeEffect
    object ShowNewsScreen : HomeEffect
    object ShowConnectScreen : HomeEffect
}