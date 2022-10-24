package com.i2asolutions.athelo.presentation.ui.authorization.signUpWithEmail

import com.i2asolutions.athelo.extensions.debugPrint
import com.i2asolutions.athelo.presentation.model.base.InputType
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.member.SignUpUseCase
import com.i2asolutions.athelo.useCase.member.StoreSessionUseCase
import com.i2asolutions.athelo.useCase.member.StoreUserEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val storeSessionUseCase: StoreSessionUseCase,
    private val storeUserEmailUseCase: StoreUserEmailUseCase,
) : BaseViewModel<SignUpEvent, SignUpEffect>() {
    private var currentState = SignUpViewState()
    private var username = ""
    private var password = ""
    private var confirmPassword = ""

    private val _viewState = MutableStateFlow(currentState)
    val viewState = _viewState.asStateFlow()

    override fun handleError(throwable: Throwable) {
        currentState = currentState.copy(isLoading = false)
        notifyStateChange()
        super.handleError(throwable)
    }

    override fun loadData() {}

    override fun handleEvent(event: SignUpEvent) {
        when (event) {
            SignUpEvent.BackButtonClick -> notifyEffectChanged(SignUpEffect.GoBack)
            SignUpEvent.SignUpClick -> selfBlockRun { signUser() }
            SignUpEvent.PPButtonClick -> notifyEffectChanged(SignUpEffect.ShowPPScreen)
            SignUpEvent.ToSButtonClick -> notifyEffectChanged(SignUpEffect.ShowToSScreen)
            is SignUpEvent.InputValueChanged -> {
                handleInputChange(event.inputValue)
            }
        }
    }

    private fun handleInputChange(input: InputType) {
        when (input) {
            is InputType.Password -> {
                password = input.value
                currentState = currentState.copy(passwordError = false)
            }
            is InputType.ConfirmPassword -> {
                confirmPassword = input.value
                currentState = currentState.copy(confirmPasswordError = false)
            }
            is InputType.Email -> {
                username = input.value
                currentState = currentState.copy(usernameError = false)
            }
            else -> {/*ignore not used types*/
            }
        }
        currentState = currentState.copy(enableButton = validate())
        notifyStateChange()
    }

    private fun notifyStateChange() {
        launchOnUI {
            _viewState.emit(currentState)
        }
    }

    private fun signUser() {
        currentState = currentState.copy(isLoading = true)
        notifyStateChange()
        if (validate()) {
            launchRequest {
                val username = this.username
                val result = signUpUseCase(username, password, confirmPassword)
                storeSessionUseCase(result.tokenData)
                storeUserEmailUseCase(username)
                withContext(Dispatchers.Main) { _effect.emit(SignUpEffect.ShowAdditionalInfoScreen) }
            }
        } else {
            currentState = currentState.copy(
                isLoading = false,
                usernameError = !validateUsername(),
                passwordError = !validatePassword(),
                confirmPasswordError = !validateConfirmPassword()
            )
            debugPrint(currentState)
            notifyStateChange()
        }
    }

    private fun validatePassword() = password.isNotBlank()
    private fun validateConfirmPassword() = confirmPassword.isNotBlank()
    private fun validateUsername() = username.isNotBlank()
    private fun validate(): Boolean {
        return validateUsername() && validatePassword() && validateConfirmPassword()
    }
}