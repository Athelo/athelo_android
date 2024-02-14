package com.athelohealth.mobile.presentation.ui.share.appointment.joinAppointment

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.appointment.GetJoinAppointmentTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class JoinAppointmentViewModel @Inject constructor(
    private val joinAppointmentTokenUseCase: GetJoinAppointmentTokenUseCase,
    savedStateHandle: SavedStateHandle
) :
    BaseViewModel<JoinAppointmentEvent, JoinAppointmentEffect, JoinAppointmentViewState>(
        JoinAppointmentViewState()
    ) {

    private val appointmentId: Int =
        JoinAppointmentFragmentArgs.fromSavedStateHandle(savedStateHandle).appointmentId
    private var sessionKey: String =
        JoinAppointmentFragmentArgs.fromSavedStateHandle(savedStateHandle).sessionKey

    private val _token = mutableStateOf("")
    val token: State<String> = _token

    private val _sessionId = mutableStateOf("")
    val sessionId: State<String> = _sessionId

    override fun pauseLoadingState() {
        notifyStateChange(currentState.copy(isLoading = false))
    }

    override fun loadData() {
        _sessionId.value = sessionKey
    }

    fun getToken() {
        if (appointmentId != -1) {
            Timber.tag("CheckCallback").d("Session ID ==> $sessionKey")
            launchRequest {
                notifyStateChange(currentState.copy(isLoading = true))
                val response = joinAppointmentTokenUseCase(appointmentId)
                pauseLoadingState()
                val token = response.token.orEmpty()
                _token.value = token
                handleEvent(JoinAppointmentEvent.JoinAppointmentTokenEvent(token))
            }
        }
    }

    override fun handleEvent(event: JoinAppointmentEvent) {
        when (event) {
            is JoinAppointmentEvent.BackPressEvent -> notifyEffectChanged(JoinAppointmentEffect.BackPressEffect)
            is JoinAppointmentEvent.JoinAppointmentTokenEvent -> {
                notifyEffectChanged(JoinAppointmentEffect.JoinAppointmentTokenEffect(event.token))
            }
        }
    }

}