package com.athelohealth.mobile.presentation.ui.share.splash

import androidx.lifecycle.viewModelScope
import com.athelohealth.mobile.presentation.model.member.AuthorizationState
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.pack.SplashUseCases
import com.athelohealth.mobile.utils.app.AppManager
import com.athelohealth.mobile.utils.app.AppType
import com.athelohealth.mobile.utils.conectivity.NetWorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val useCases: SplashUseCases,
    private val appManager: AppManager,
) :
    BaseViewModel<SplashEvent, SplashEffect>() {
    private var currentState = SplashViewState(showPin = useCases.showPin())
    private var showTutorial: Boolean = false

    private val _stateView = MutableStateFlow(currentState)
    val state = _stateView.asStateFlow()

    init {
        launchRequest {
            showTutorial = useCases.showTutorial()
        }
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
            launchRequest {
                if (!NetWorkManager.isDisconnected) {
                    useCases.getUserStateUseCase().onEach { state ->
                        when (state) {
                            AuthorizationState.Unknown -> loadData()
                            AuthorizationState.Authorized -> continueAfterAuthorization()
                            AuthorizationState.Unauthorized -> showAuthorizeScreen()
                        }
                    }.launchIn(viewModelScope)
                    useCases.userAuthorizationCheckUseCase()
                }
            }
        }
        SplashEvent.ReloadData -> {
            loadData()
        }
        is SplashEvent.EnterPin -> {
            launchRequest {
                if (event.pin.length >= 4) {
                    if (useCases.verifyPin(event.pin)) {
                        withContext(Dispatchers.Main) {
                            currentState = currentState.copy(showPin = false)
                            notifyChange()
                        }
                    } else {
                        errorMessage("Invalid access code. Please try again.")
                    }
                }
            }
        }
    }

    private fun continueAfterAuthorization() {
        launchRequest {
            useCases.getEnumsUseCase()
            useCases.connectWebSocketUseCase()
            val user = useCases.setupPersonalConfigUseCase()
            if (appManager.appType.value.javaClass == AppType.Caregiver::class.java && user?.isCaregiver != true) {
                appManager.changeAppType(AppType.Unknown)
            }
            withContext(Dispatchers.Main) {
                _effect.emit(
                    if (showTutorial) SplashEffect.ShowTutorialScreen
                    else if (user == null)
                        SplashEffect.ShowAdditionalInformationScreen
                    else if (appManager.appType.value == AppType.Unknown) {
                        SplashEffect.ShowActAsScreen(initFlow = true)
                    } else
                        SplashEffect.ShowHomeScreen
                )
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