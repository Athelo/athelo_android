package com.i2asolutions.athelo.presentation.ui.menu

import com.i2asolutions.athelo.presentation.model.menu.MenuItem
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class MenuViewState(
    override val isLoading: Boolean = false,
    val items: List<MenuItem>,
    val displayName: String,
    val image: String?,
    val unreadMessages: Boolean
) : BaseViewState

sealed interface MenuEvent : BaseEvent {
    data class ItemClick(val deeplink: String) : MenuEvent
    object UserClick : MenuEvent
    object CloseClick : MenuEvent
}

sealed interface MenuEffect: BaseEffect {
    object CloseMenuScreen: MenuEffect
    object ShowMyProfileScreen: MenuEffect
    object ShowMessagesScreen: MenuEffect
    object ShowMySymptomsScreen: MenuEffect
    object ShowMyCaregiversScreen: MenuEffect
    object ShowInviteCaregiver: MenuEffect
    object ShowSettingsScreen: MenuEffect
    class ShowConnectSmartWatchScreen(val url: String) : MenuEffect
    object ShowAskAtheloScreen: MenuEffect
    object ShowSendFeedbackScreen: MenuEffect
}