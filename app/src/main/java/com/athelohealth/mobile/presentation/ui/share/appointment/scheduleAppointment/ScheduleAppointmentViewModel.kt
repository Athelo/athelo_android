package com.athelohealth.mobile.presentation.ui.share.appointment.scheduleAppointment

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.appointment.LoadProvidersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleAppointmentViewModel @Inject constructor(
    private val providersUseCase: LoadProvidersUseCase
) : BaseViewModel<ScheduleAppointmentEvent, ScheduleAppointmentEffect, ScheduleAppointmentViewState>(
    ScheduleAppointmentViewState()
) {

    private var nextUrl: String? = null

    override fun pauseLoadingState() {
        notifyStateChange(currentState.copy(isLoading = false))
    }

    override fun loadData() {
        notifyStateChange(currentState.copy(isLoading = true))
        viewModelScope.launch {
            delay(500)
            pauseLoadingState()
        }
       /* launchRequest {
            val result = providersUseCase()
            Log.d("ApiDataSize", "ViewModel: ${result.result.size}")
            nextUrl = result.next
            //pauseLoadingState()
            notifyStateChange(currentState.copy(
                isLoading = false,
                providers = result.result.first().providers ?: emptyList()
            ))
        }*/
    }

    override fun handleEvent(event: ScheduleAppointmentEvent) {
        when(event) {
            is ScheduleAppointmentEvent.OnBackButtonClicked -> {
                notifyEffectChanged(ScheduleAppointmentEffect.ShowPrevScreen)
            }

            is ScheduleAppointmentEvent.OnAppointmentScheduled -> {
                notifyEffectChanged(ScheduleAppointmentEffect.ShowSuccessMessage(event.msg))
            }
        }
    }

    fun displaySuccessMessage(message: String) {
        sendBaseEvent(BaseEvent.DisplaySuccess(message))
    }
}