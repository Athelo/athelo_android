package com.athelohealth.mobile.presentation.ui.share.appointment.scheduleAppointment

import androidx.lifecycle.viewModelScope
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ScheduleAppointmentViewModel: BaseViewModel<ScheduleAppointmentEvent, ScheduleAppointmentEffect, ScheduleAppointmentViewState>(
    ScheduleAppointmentViewState()
) {
    override fun pauseLoadingState() {
        notifyStateChange(currentState.copy(isLoading = false))
    }

    override fun loadData() {
        notifyStateChange(currentState.copy(isLoading = true))
        //Todo Hit the api to fetch appointment lists
        viewModelScope.launch {
            delay(1000)
            pauseLoadingState()
        }
    }

    override fun handleEvent(event: ScheduleAppointmentEvent) {
        when(event) {
            is ScheduleAppointmentEvent.OnBackButtonClicked -> {
                notifyEffectChanged(ScheduleAppointmentEffect.ShowPrevScreen)
            }
        }
    }
}