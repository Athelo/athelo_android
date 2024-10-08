package com.i2asolutions.athelo.presentation.ui.connectFitbit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.i2asolutions.athelo.presentation.model.member.User
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.connectFitbit.CheckFitbitConnectionStateUseCase
import com.i2asolutions.athelo.useCase.connectFitbit.ListenFitbitConnectionStateUseCase
import com.i2asolutions.athelo.useCase.health.FitbitAuthorizationUseCase
import com.i2asolutions.athelo.useCase.member.LoadMyProfileUseCase
import com.i2asolutions.athelo.useCase.member.StoreUserUseCase
import com.i2asolutions.athelo.utils.fitbit.FitbitState
import com.i2asolutions.athelo.utils.fitbit.toScreenType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
) : BaseViewModel<ConnectFitbitEvent, ConnectFitbitEffect>() {
    private var currentState = ConnectFitbitViewState(
        displaySkipButton = ConnectFitbitFragmentArgs.fromSavedStateHandle(savedStateHandle).showSkipButton
    )
    private var sendMessage: Boolean = false

    private val _state = MutableStateFlow(currentState)
    val state = _state.asStateFlow()

    init {
        listenFitbitConnection().onEach {
            notifyStateChanged(
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

    override fun loadData() {
        showLoading()
        launchRequest {
            notifyStateChanged(
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
        notifyStateChanged(currentState.copy(isLoading = true))
    }

    private fun hideLoading() {
        notifyStateChanged(currentState.copy(isLoading = false))
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

    private fun notifyStateChanged(newState: ConnectFitbitViewState) {
        currentState = newState
        launchOnUI { _state.emit(currentState) }
    }
}