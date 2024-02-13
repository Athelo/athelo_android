package com.athelohealth.mobile.presentation.ui.share.appointment.joinAppointment

import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

data class JoinAppointmentViewState(
    override val isLoading: Boolean = false
): BaseViewState

sealed interface JoinAppointmentEvent: BaseEvent {
    object BackPressEvent: JoinAppointmentEvent
    data class JoinAppointmentTokenEvent(val token: String): JoinAppointmentEvent
}

sealed interface JoinAppointmentEffect: BaseEffect {
    object BackPressEffect: JoinAppointmentEffect
    data class JoinAppointmentTokenEffect(val token: String): JoinAppointmentEffect
}
