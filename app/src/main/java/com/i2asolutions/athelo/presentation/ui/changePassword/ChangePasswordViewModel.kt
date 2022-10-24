package com.i2asolutions.athelo.presentation.ui.changePassword

import com.i2asolutions.athelo.presentation.model.base.InputType
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.member.ChangePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(private val changePasswordUseCase: ChangePasswordUseCase) :
    BaseViewModel<ChangePasswordEvent, ChangePasswordEffect>() {
    private var currentState = ChangePasswordViewState()

    private val _state = MutableStateFlow(currentState)
    val state = _state.asStateFlow()

    override fun handleError(throwable: Throwable) {
        notifyStateChanged(currentState.copy(isLoading = false))
        super.handleError(throwable)
    }

    override fun loadData() {

    }

    override fun handleEvent(event: ChangePasswordEvent) {
        when (event) {
            ChangePasswordEvent.BackButtonClick -> notifyEffectChanged(ChangePasswordEffect.ShowPrevScreen)
            is ChangePasswordEvent.InputChanged -> handleInputChange(event.inputType)
            ChangePasswordEvent.SendButtonClick -> sendNewPasswordReq()
        }
    }

    private fun validate(
        currentPassword: String,
        newPassword: String,
        repeatNewPassword: String
    ): Boolean =
        currentPassword.isNotBlank() && newPassword.isNotBlank() && newPassword == repeatNewPassword

    private fun handleInputChange(input: InputType) {
        when (input) {
            is InputType.ConfirmPassword -> {
                notifyStateChanged(
                    currentState.copy(
                        enableButton = validate(
                            currentPassword = currentState.currentPassword,
                            newPassword = currentState.newPassword,
                            repeatNewPassword = input.value
                        ), repeatNewPassword = input.value
                    )
                )
            }
            is InputType.NewPassword -> {
                notifyStateChanged(
                    currentState.copy(
                        enableButton = validate(
                            currentPassword = currentState.currentPassword,
                            newPassword = input.value,
                            repeatNewPassword = currentState.repeatNewPassword
                        ),
                        newPassword = input.value
                    )
                )
            }
            is InputType.Password -> {
                notifyStateChanged(
                    currentState.copy(
                        enableButton = validate(
                            currentPassword = input.value,
                            newPassword = currentState.newPassword,
                            repeatNewPassword = currentState.repeatNewPassword
                        ),
                        currentPassword = input.value
                    )
                )
            }
            else -> {/*ignore other type*/
            }
        }

    }

    private fun sendNewPasswordReq() = selfBlockRun {
        val (currentPassword, newPassword, repeatPassword) = currentState
        if (validate(
                currentPassword = currentPassword,
                newPassword = newPassword,
                repeatNewPassword = repeatPassword
            )
        ) {
            notifyStateChanged(currentState.copy(isLoading = true))
            launchRequest {
                val result = changePasswordUseCase(
                    newPassword = newPassword,
                    repeatNewPassword = repeatPassword,
                    oldPassword = currentPassword
                )
                notifyStateChanged(
                    currentState.copy(
                        isLoading = false,
                        currentPassword = "",
                        repeatNewPassword = "",
                        newPassword = "",
                        enableButton = false
                    )
                )
                successMessage(result)
            }
        }
    }

    private fun notifyStateChanged(state: ChangePasswordViewState) {
        currentState = state
        launchOnUI { _state.emit(currentState) }
    }
}