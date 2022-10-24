package com.i2asolutions.athelo.presentation.ui.appInfo

import androidx.annotation.StringRes
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class AppInfoViewState(
    override val isLoading: Boolean,
    @StringRes val screenName: Int,
    val text: String
) : BaseViewState

sealed interface AppInfoEvent : BaseEvent {
    object BackClicked : AppInfoEvent
    class LinkClicked(val url: String) : AppInfoEvent
}

sealed interface AppInfoEffect : BaseEffect {
    object GoBack : AppInfoEffect
    class OpenUrl(val url: String) : AppInfoEffect
    class OpenEmail(val email: String) : AppInfoEffect
}