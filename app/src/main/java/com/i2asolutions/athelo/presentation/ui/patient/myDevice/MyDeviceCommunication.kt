package com.i2asolutions.athelo.presentation.ui.patient.myDevice

import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class MyDeviceViewState(
    override val isLoading: Boolean = false,
    val showDisconnectConfirmation: Boolean = false,
    val showForceDisconnectConfirmation: Boolean = false,
) : BaseViewState

sealed interface MyDeviceEvent : BaseEvent {
    object BackButtonClick : MyDeviceEvent
    object DisconnectClick : MyDeviceEvent
    object DisconnectConfirmed : MyDeviceEvent
    object ForceDisconnectConfirmed : MyDeviceEvent
    object PopupCancelButtonClick : MyDeviceEvent
}

sealed interface MyDeviceEffect : BaseEffect {
    object ShowPrevScreen : MyDeviceEffect
}