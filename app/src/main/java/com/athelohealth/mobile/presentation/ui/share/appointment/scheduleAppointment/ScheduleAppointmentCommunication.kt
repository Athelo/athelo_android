package com.athelohealth.mobile.presentation.ui.share.appointment.scheduleAppointment

import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

data class ScheduleAppointmentViewState(
    override val isLoading: Boolean = false,
): BaseViewState

sealed interface ScheduleAppointmentEvent: BaseEvent {
    object OnBackButtonClicked: ScheduleAppointmentEvent
}

sealed interface ScheduleAppointmentEffect: BaseEffect {

}