package com.athelohealth.mobile.presentation.ui.mainActivity

import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent

sealed interface MainActivityEffect : BaseEffect

sealed interface MainActivityEvent : BaseEvent {
    object ConnectWebSockets : MainActivityEvent
    object DisconnectWebSockets : MainActivityEvent
    data class SuccessMessageReceive(val message: String) : MainActivityEvent
    data class ErrorMessageReceive(val message: String) : MainActivityEvent
    data class NormalMessageReceive(val message: String) : MainActivityEvent
}