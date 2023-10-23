package com.i2asolutions.athelo.presentation.ui.mainActivity

import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent

sealed interface MainActivityEffect : BaseEffect

sealed interface MainActivityEvent : BaseEvent {
    object ConnectWebSockets : MainActivityEvent
    object DisconnectWebSockets : MainActivityEvent
    data class SuccessMessageReceive(val message: String) : MainActivityEvent
    data class ErrorMessageReceive(val message: String) : MainActivityEvent
    data class NormalMessageReceive(val message: String) : MainActivityEvent
}