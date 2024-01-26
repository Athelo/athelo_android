package com.athelohealth.mobile.presentation.ui.share.appointment.scheduleAppointment

import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState
import com.athelohealth.mobile.presentation.ui.patient.wellbeing.WellbeingEffect

data class ScheduleAppointmentViewState(
    override val isLoading: Boolean = false,
): BaseViewState

sealed interface ScheduleAppointmentEvent: BaseEvent {
    object OnBackButtonClicked: ScheduleAppointmentEvent
}

sealed interface ScheduleAppointmentEffect: BaseEffect {
    object ShowPrevScreen : ScheduleAppointmentEffect
}