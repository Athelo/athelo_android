package com.i2asolutions.athelo.presentation.ui.myDevice

import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.connectFitbit.CheckFitbitConnectionStateUseCase
import com.i2asolutions.athelo.useCase.health.DisconnectFitbitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MyDeviceViewModel @Inject constructor(
    private val disconnectFitbit: DisconnectFitbitUseCase,
    private val checkFitbitConnection: CheckFitbitConnectionStateUseCase,
    ) : BaseViewModel<MyDeviceEvent, MyDeviceEffect>() {
    private var currentState = MyDeviceViewState()

    private val _state = MutableStateFlow(currentState)
    val state = _state.asStateFlow()

    override fun handleError(throwable: Throwable) {
        notifyStateChanged(currentState.copy(isLoading = false))
        super.handleError(throwable)
    }

    override fun loadData() {

    }

    override fun handleEvent(event: MyDeviceEvent) {
        when (event) {
            MyDeviceEvent.BackButtonClick -> notifyEffectChanged(MyDeviceEffect.ShowPrevScreen)
            MyDeviceEvent.DisconnectClick -> notifyStateChanged(
                currentState.copy(
                    showDisconnectConfirmation = true
                )
            )
            MyDeviceEvent.DisconnectConfirmed -> launchRequest {
                notifyStateChanged(currentState.copy(isLoading = true))
                if (!disconnectFitbit()) {
                    notifyStateChanged(
                        currentState.copy(
                            isLoading = false,
                            showDisconnectConfirmation = false
                        )
                    )
                    error("Disconnecting your account failed. Please try again latter")
                } else {
                    checkFitbitConnection()
                    notifyEffectChanged(MyDeviceEffect.ShowPrevScreen)
                }
            }
            MyDeviceEvent.PopupCancelButtonClick -> notifyStateChanged(
                currentState.copy(
                    isLoading = false,
                    showDisconnectConfirmation = false
                )
            )
        }
    }

    private fun notifyStateChanged(newState: MyDeviceViewState) {
        currentState = newState
        launchOnUI { _state.emit(currentState) }
    }
}