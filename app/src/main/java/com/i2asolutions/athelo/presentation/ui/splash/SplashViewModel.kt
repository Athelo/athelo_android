package com.i2asolutions.athelo.presentation.ui.splash

import androidx.lifecycle.viewModelScope
import com.i2asolutions.athelo.presentation.model.member.AuthorizationState
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.pack.SplashUseCases
import com.i2asolutions.athelo.utils.conectivity.NetWorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val useCases: SplashUseCases) :
    BaseViewModel<SplashEvent, SplashEffect>() {
    private var currentState = SplashViewState()
    private var showTutorial: Boolean = false

    private val _stateView = MutableStateFlow(currentState)
    val state = _stateView.asStateFlow()

    init {
        launchRequest {
            showTutorial = useCases.showTutorial()
        }
        useCases.getUserStateUseCase().onEach { state ->
            when (state) {
                AuthorizationState.Unknown -> loadData()
                AuthorizationState.Authorized -> continueAfterAuthorization()
                AuthorizationState.Unauthorized -> showAuthorizeScreen()
            }
        }.launchIn(viewModelScope)
    }

    override fun loadData() {
        if (NetWorkManager.isDisconnected) errorNoInternet()
        else {
            launchRequest { useCases.getEnumsUseCase() }
            launchRequest { useCases.userAuthorizationCheckUseCase() }
        }
    }

    override fun handleEvent(event: SplashEvent) = when (event) {
        SplashEvent.InitApp -> {
            launchRequest { if (!NetWorkManager.isDisconnected) useCases.userAuthorizationCheckUseCase() }
        }
        SplashEvent.ReloadData -> {
            loadData()
        }
    }

    private fun continueAfterAuthorization() {
        launchRequest {
            useCases.getEnumsUseCase()
            useCases.connectWebSocketUseCase()
            val user = useCases.setupPersonalConfigUseCase()
            withContext(Dispatchers.Main) {
                _effect.emit(if (showTutorial) SplashEffect.ShowTutorialScreen else if (user == null) SplashEffect.ShowAdditionalInformationScreen else SplashEffect.ShowHomeScreen)
            }
        }
    }

    private fun showAuthorizeScreen() {
        launchRequest {
            useCases.getEnumsUseCase()
            withContext(Dispatchers.Main) {
                _effect.emit(if (showTutorial) SplashEffect.ShowTutorialScreen else SplashEffect.ShowAuthorizationScreen)
            }
        }
    }

    private fun notifyChange() {
        launchOnUI {
            _stateView.emit(currentState)
        }
    }
}