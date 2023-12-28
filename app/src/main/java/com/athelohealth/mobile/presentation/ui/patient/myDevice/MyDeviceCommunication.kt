package com.athelohealth.mobile.presentation.ui.patient.myDevice

import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

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