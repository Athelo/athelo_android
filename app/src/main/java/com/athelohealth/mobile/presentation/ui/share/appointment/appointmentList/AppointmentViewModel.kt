package com.athelohealth.mobile.presentation.ui.share.appointment.appointmentList

import com.athelohealth.mobile.extensions.update
import com.athelohealth.mobile.presentation.model.appointment.Appointments
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.appointment.LoadAppointmentsUseCase
import com.athelohealth.mobile.useCase.member.LoadCachedUserUseCase
import com.athelohealth.mobile.useCase.member.LoadMyProfileUseCase
import com.athelohealth.mobile.useCase.member.StoreUserUseCase
import com.athelohealth.mobile.utils.AuthorizationException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val loadMyProfileUseCase: LoadMyProfileUseCase,
    private val storeProfile: StoreUserUseCase,
    private val loadCachedUserUseCase: LoadCachedUserUseCase,
    private val loadAppointmentsUseCase: LoadAppointmentsUseCase
) : BaseViewModel<AppointmentEvent, AppointmentEffect, AppointmentViewState>(AppointmentViewState()) {

    private val _appointments = MutableStateFlow(listOf<Appointments.Appointment>())
    val appointments = _appointments.asStateFlow()

    private val _isAppointmentDeleted = MutableStateFlow(false)
    val isAppointmentDeleted = _isAppointmentDeleted.asStateFlow()

    override fun pauseLoadingState() {
        notifyStateChange(currentState.copy(isLoading = false))
    }

    override fun loadData() {}

    fun getAppointments() {
        changeDeletedAppointmentState(false)
        notifyStateChange(currentState.copy(isLoading = true))
        launchRequest {
            val user =
                loadCachedUserUseCase() ?: loadMyProfileUseCase().also { storeProfile(it) }
                ?: throw AuthorizationException()

            // Get Appointment List from the API
            val response = loadAppointmentsUseCase()
            _appointments.emit(response.appointments ?: emptyList())

            // Update States
            notifyStateChange(
                currentState.copy(
                    initialized = true,
                    isLoading = false,
                    user = user
                )
            )
        }
    }

    private fun deleteAppointment(appointmentId: Int) {
        launchRequest {
            val isAppointmentDeleted = loadAppointmentsUseCase(appointmentId)
            if (isAppointmentDeleted) {
                val newList = mutableListOf<Appointments.Appointment>()
                _appointments.update { list ->
                    list.forEach {
                        if (it.id != appointmentId) newList.add(it)
                    }
                    return@update newList
                }
            }
            changeDeletedAppointmentState(isAppointmentDeleted)
        }
    }

    private fun changeDeletedAppointmentState(state: Boolean) {
        launchRequest {
            _isAppointmentDeleted.emit(state)
        }
    }

    override fun handleEvent(event: AppointmentEvent) {
        when (event) {
            is AppointmentEvent.MenuClick -> {
                notifyEffectChanged(AppointmentEffect.ShowMenuScreen)
            }

            is AppointmentEvent.MyProfileClick -> {
                notifyEffectChanged(AppointmentEffect.ShowMyProfileScreen)
            }

            is AppointmentEvent.ScheduleMyAppointmentClick -> {
                notifyEffectChanged(AppointmentEffect.ShowScheduleMyAppointment)
            }

            is AppointmentEvent.ShowSuccessMessage -> {
                sendBaseEvent(BaseEvent.DisplaySuccess(event.msg))
            }

            is AppointmentEvent.DeleteAppointment -> {
                deleteAppointment(event.appointmentId)
            }

            is AppointmentEvent.JoinAppointmentClick -> {
                notifyEffectChanged(AppointmentEffect.JoinAppointment(
                    event.appointmentId, event.sessionKey
                ))
            }
        }
    }
}