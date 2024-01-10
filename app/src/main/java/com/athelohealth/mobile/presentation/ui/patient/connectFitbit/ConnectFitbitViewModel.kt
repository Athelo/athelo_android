package com.athelohealth.mobile.presentation.ui.patient.connectFitbit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.athelohealth.mobile.presentation.model.member.User
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.connectFitbit.CheckFitbitConnectionStateUseCase
import com.athelohealth.mobile.useCase.connectFitbit.ListenFitbitConnectionStateUseCase
import com.athelohealth.mobile.useCase.health.FitbitAuthorizationUseCase
import com.athelohealth.mobile.useCase.member.LoadMyProfileUseCase
import com.athelohealth.mobile.useCase.member.StoreUserUseCase
import com.athelohealth.mobile.utils.fitbit.FitbitState
import com.athelohealth.mobile.utils.fitbit.toScreenType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ConnectFitbitViewModel @Inject constructor(
    listenFitbitConnection: ListenFitbitConnectionStateUseCase,
    private val checkFitbitConnectionState: CheckFitbitConnectionStateUseCase,
    private val loadProfile: LoadMyProfileUseCase,
    private val storeUser: StoreUserUseCase,
    private val fitbitInit: FitbitAuthorizationUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ConnectFitbitEvent, ConnectFitbitEffect, ConnectFitbitViewState>(ConnectFitbitViewState(
    displaySkipButton = false
)) {

    init {
        notifyStateChange(
            ConnectFitbitViewState(
                displaySkipButton = ConnectFitbitFragmentArgs.fromSavedStateHandle(savedStateHandle).showSkipButton
            )
        )
    }
    private var sendMessage: Boolean = false

    init {
        listenFitbitConnection().onEach {
            notifyStateChange(
                currentState.copy(
                    isLoading = false,
                    screenType = it.toScreenType()
                )
            )
            if (sendMessage) {
                launchOnUI {
                    when (it) {
                        FitbitState.Connected -> successMessage("Device found and connected successfully!")
                        is FitbitState.Failure -> errorMessage("Something went wrong, please try again")
                        FitbitState.Unknown -> {}
                    }
                }
            }
        }.launchIn(viewModelScope)
        launchRequest {
            checkFitbitConnectionState()
        }
    }

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    override fun loadData() {
        showLoading()
        launchRequest {
            notifyStateChange(
                currentState.copy(
                    currentUser = loadProfile().also { storeUser(it) } ?: User(),
                    isLoading = false
                )
            )
        }
    }

    override fun handleError(throwable: Throwable) {
        hideLoading()
        super.handleError(throwable)
    }

    private fun showLoading() {
        notifyStateChange(currentState.copy(isLoading = true))
    }

    private fun hideLoading() {
        notifyStateChange(currentState.copy(isLoading = false))
    }

    override fun handleEvent(event: ConnectFitbitEvent) {
        when (event) {
            ConnectFitbitEvent.ConnectButtonClick -> launchRequest {
                sendMessage = true
                notifyEffectChanged(
                    ConnectFitbitEffect.ShowConnectFitbitScreen(fitbitInit())
                )
            }
            ConnectFitbitEvent.GoToActivityPageClick -> notifyEffectChanged(ConnectFitbitEffect.ShowActivityPageScreen)
            ConnectFitbitEvent.MenuClick -> notifyEffectChanged(ConnectFitbitEffect.ShowMenuScreen)
            ConnectFitbitEvent.MyProfileClick -> notifyEffectChanged(ConnectFitbitEffect.ShowMyProfileScreen)
            ConnectFitbitEvent.SkipButtonClick -> notifyEffectChanged(ConnectFitbitEffect.ShowHomePageScreen)
            ConnectFitbitEvent.BackButtonClick -> notifyEffectChanged(ConnectFitbitEffect.ShowPrevScreen)
        }
    }
}