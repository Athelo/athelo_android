package com.athelohealth.mobile.presentation.ui.share.appointment.scheduleAppointment

import com.athelohealth.mobile.presentation.ui.base.BaseViewModel

class ScheduleAppointmentViewModel: BaseViewModel<ScheduleAppointmentEvent, ScheduleAppointmentEffect, ScheduleAppointmentViewState>(
    ScheduleAppointmentViewState()
) {
    override fun pauseLoadingState() {
        notifyStateChange(currentState.copy(isLoading = false))
    }

    override fun loadData() {
        notifyStateChange(currentState.copy(isLoading = true))
        //Todo Hit the api to fetch appointment lists
    }
}