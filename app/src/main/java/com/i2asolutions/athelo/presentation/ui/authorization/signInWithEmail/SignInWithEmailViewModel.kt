package com.i2asolutions.athelo.presentation.ui.authorization.signInWithEmail

import com.i2asolutions.athelo.presentation.model.base.InputType
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.SetupPersonalConfigUseCase
import com.i2asolutions.athelo.useCase.member.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignInWithEmailViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val setupPersonalInfo: SetupPersonalConfigUseCase,
) :
    BaseViewModel<SignInWithEmailEvent, SignInWithEmailEffect>() {
    private var username: String = ""
    private var password: String = ""

    private var currentViewState =
        SignInWithEmailViewState(enableButton = false, usernameError = false, passwordError = false)
    private val _state = MutableStateFlow(currentViewState)
    val state = _state.asStateFlow()

    override fun loadData() {}

    override fun handleError(throwable: Throwable) {
        super.handleError(throwable)
        currentViewState = currentViewState.copy(isLoading = false)
        notifyStateChange()
    }

    override fun handleEvent(event: SignInWithEmailEvent) {
        when (event) {
            SignInWithEmailEvent.BackButtonClick -> notifyEffectChanged(SignInWithEmailEffect.GoBack)
            SignInWithEmailEvent.ForgotPasswordClick -> selfBlockRun {
                currentViewState = currentViewState.copy(username = username)
                notifyEffectChanged(
                    SignInWithEmailEffect.ShowForgotPassword(username)
                )
            }
            is SignInWithEmailEvent.InputValueChanged -> handleInputChange(event.inputType)
            SignInWithEmailEvent.SignInClicked -> selfBlockRun {
                signIn()
            }
            SignInWithEmailEvent.PPButtonClick -> notifyEffectChanged(SignInWithEmailEffect.ShowPrivacyPolicyScreen)
            SignInWithEmailEvent.ToSButtonClick -> notifyEffectChanged(SignInWithEmailEffect.ShowTermsOfUseScreen)
        }
    }

    private fun signIn() {
        if (validate()) {
            currentViewState = currentViewState.copy(isLoading = true)
            notifyStateChange()
            launchRequest(SupervisorJob() + Dispatchers.IO + requestExceptionHandler) {
                val result = signInUseCase(username, password)
                val profile = setupPersonalInfo(result.tokenData, username)
                if (profile == null)
                    withContext(Dispatchers.Main) { _effect.emit(SignInWithEmailEffect.ShowAdditionalInformationScreen) }
                else withContext(Dispatchers.Main) { _effect.emit(SignInWithEmailEffect.ShowHomeScreen) }
            }
        } else {
            currentViewState = currentViewState.copy(
                usernameError = !validateUsername(),
                passwordError = !validatePassword()
            )
        }
    }

    private fun handleInputChange(inputType: InputType) {
        when (inputType) {
            is InputType.Password -> {
                password = inputType.value
                currentViewState = currentViewState.copy(passwordError = false)
            }
            is InputType.Email -> {
                username = inputType.value
                currentViewState = currentViewState.copy(usernameError = false)
            }
            else -> {}
        }
        currentViewState = currentViewState.copy(enableButton = validate())
        notifyStateChange()
    }

    private fun notifyStateChange() {
        launchOnUI {
            _state.emit(currentViewState)
        }
    }

    private fun validatePassword() = password.isNotBlank()
    private fun validateUsername() = username.isNotBlank()
    private fun validate(): Boolean {
        return validateUsername() && validatePassword()
    }
}