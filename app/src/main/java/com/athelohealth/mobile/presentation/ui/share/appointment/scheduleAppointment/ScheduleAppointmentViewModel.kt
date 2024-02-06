package com.athelohealth.mobile.presentation.ui.share.appointment.scheduleAppointment

import com.athelohealth.mobile.presentation.model.appointment.Provider
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.appointment.LoadProvidersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ScheduleAppointmentViewModel @Inject constructor(
    private val providersUseCase: LoadProvidersUseCase
) : BaseViewModel<ScheduleAppointmentEvent, ScheduleAppointmentEffect, ScheduleAppointmentViewState>(
    ScheduleAppointmentViewState()
) {

    private val _contentfulViewState = MutableStateFlow(listOf<Provider>())
    val contentfulViewState = _contentfulViewState.asStateFlow()

    private val _providersAvailability = MutableStateFlow(listOf<String>())
    val providersAvailability = _providersAvailability.asStateFlow()

    override fun pauseLoadingState() {
        notifyStateChange(currentState.copy(isLoading = false))
    }

    override fun loadData() {
        notifyStateChange(currentState.copy(isLoading = true))
        launchRequest {
            val response = providersUseCase()
            pauseLoadingState()
            _contentfulViewState.emit(response.providers ?: emptyList())
        }
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

    fun getProvidersAvailability(date: String) {
        notifyStateChange(currentState.copy(isLoading = true))
        launchRequest {
            val response = providersUseCase(date)
            pauseLoadingState()
            _providersAvailability.emit(response.results ?: emptyList())
        }
    }
}