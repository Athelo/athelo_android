package com.athelohealth.mobile.presentation.ui.patient.myDevice

import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.connectFitbit.CheckFitbitConnectionStateUseCase
import com.athelohealth.mobile.useCase.health.DisconnectFitbitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class MyDeviceViewModel @Inject constructor(
    private val disconnectFitbit: DisconnectFitbitUseCase,
    private val checkFitbitConnection: CheckFitbitConnectionStateUseCase,
) : BaseViewModel<MyDeviceEvent, MyDeviceEffect, MyDeviceViewState>(MyDeviceViewState()) {

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }
    override fun loadData() {}

    override fun handleEvent(event: MyDeviceEvent) {
        when (event) {
            MyDeviceEvent.BackButtonClick -> notifyEffectChanged(MyDeviceEffect.ShowPrevScreen)
            MyDeviceEvent.DisconnectClick -> notifyStateChange(
                currentState.copy(
                    showDisconnectConfirmation = true
                )
            )
            MyDeviceEvent.DisconnectConfirmed -> launchRequest(context = Dispatchers.IO + CoroutineExceptionHandler { _, _ ->
                notifyStateChange(
                    currentState.copy(
                        isLoading = false,
                        showDisconnectConfirmation = false,
                        showForceDisconnectConfirmation = true
                    )
                )
            }) {
                notifyStateChange(currentState.copy(isLoading = true))
                if (!disconnectFitbit()) {
                    notifyStateChange(
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
            MyDeviceEvent.PopupCancelButtonClick -> notifyStateChange(
                currentState.copy(
                    isLoading = false,
                    showDisconnectConfirmation = false,
                    showForceDisconnectConfirmation = false,
                )
            )
            MyDeviceEvent.ForceDisconnectConfirmed -> launchRequest {
                notifyStateChange(currentState.copy(isLoading = true))
                if (!disconnectFitbit(true)) {
                    notifyStateChange(
                        currentState.copy(
                            isLoading = false,
                            showDisconnectConfirmation = false
                        )
                    )
                    error("Disconnecting your account failed. Please try again later")
                } else {
                    checkFitbitConnection()
                    notifyEffectChanged(MyDeviceEffect.ShowPrevScreen)
                }
            }
        }
    }
}

