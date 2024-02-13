package com.athelohealth.mobile.presentation.ui.share.appointment.joinAppointment

import android.view.View
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.appointment.GetJoinAppointmentTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class JoinAppointmentViewModel @Inject constructor(
    private val joinAppointmentTokenUseCase: GetJoinAppointmentTokenUseCase,
    savedStateHandle: SavedStateHandle
) :
    BaseViewModel<JoinAppointmentEvent, JoinAppointmentEffect, JoinAppointmentViewState>(
        JoinAppointmentViewState()
    ) {

    private val appointmentId: Int = JoinAppointmentFragmentArgs.fromSavedStateHandle(savedStateHandle).appointmentId
    private var sessionKey: String = JoinAppointmentFragmentArgs.fromSavedStateHandle(savedStateHandle).sessionKey

    private val _publisherView = mutableStateOf<View?>(null)
    val publisherView: State<View?> = _publisherView

    private val _subscriberView = mutableStateOf<View?>(null)
    val subscriberView: State<View?> = _subscriberView

    private val _removeAllSubscriberView = mutableStateOf(false)
    val removeAllSubscriberView: State<Boolean> = _removeAllSubscriberView

    private val _token = mutableStateOf("")
    val token: State<String> = _token

    private val _sessionId = mutableStateOf("")
    val sessionId: State<String> = _sessionId

    override fun pauseLoadingState() {
        notifyStateChange(currentState.copy(isLoading = false))
    }

    override fun loadData() {
        getToken(appointmentId)
    }

    private fun getToken(appointmentId: Int?) {
        if(appointmentId != null) {
            _sessionId.value = sessionKey
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
        when(event) {
            is JoinAppointmentEvent.BackPressEvent -> notifyEffectChanged(JoinAppointmentEffect.BackPressEffect)
            is JoinAppointmentEvent.JoinAppointmentTokenEvent -> {
                notifyEffectChanged(JoinAppointmentEffect.JoinAppointmentTokenEffect(event.token))
            }
        }
    }

    fun setPublisherView(view: View?) {
        _publisherView.value = view
    }

    fun setSubscriberView(view: View?) {
        _subscriberView.value = view
    }

    fun removeAllViewOfSubscriber() {
        _removeAllSubscriberView.value = true
    }
}