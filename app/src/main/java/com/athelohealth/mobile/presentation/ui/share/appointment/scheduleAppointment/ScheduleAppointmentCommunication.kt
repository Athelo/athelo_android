package com.athelohealth.mobile.presentation.ui.share.appointment.scheduleAppointment

import com.athelohealth.mobile.presentation.model.appointment.Provider
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

data class ScheduleAppointmentViewState(
    override val isLoading: Boolean = false,
    val providers: List<Provider> = emptyList(),
    val canLoadMore: Boolean = false
): BaseViewState

sealed interface ScheduleAppointmentEvent: BaseEvent {
    data class OnBackButtonClicked(val shouldLoadAppointments: Boolean): ScheduleAppointmentEvent
    data class OnAppointmentScheduled(val msg: String): ScheduleAppointmentEvent
}

sealed interface ScheduleAppointmentEffect: BaseEffect {
    data class ShowPrevScreen(val shouldLoadAppointments: Boolean) : ScheduleAppointmentEffect
    data class ShowSuccessMessage(val msg: String): ScheduleAppointmentEffect
}