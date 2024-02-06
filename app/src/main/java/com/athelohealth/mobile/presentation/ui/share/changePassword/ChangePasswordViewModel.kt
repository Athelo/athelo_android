package com.athelohealth.mobile.presentation.ui.share.changePassword

import com.athelohealth.mobile.presentation.model.base.InputType
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.member.ChangePasswordUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(private val changePasswordUseCase: ChangePasswordUseCase) :
    BaseViewModel<ChangePasswordEvent, ChangePasswordEffect, ChangePasswordViewState>(ChangePasswordViewState()) {

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

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
                notifyStateChange(
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
                notifyStateChange(
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
                notifyStateChange(
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
            notifyStateChange(currentState.copy(isLoading = true))
            FirebaseAuth.getInstance().currentUser?.updatePassword(newPassword)?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        notifyStateChange(
                            currentState.copy(
                                isLoading = false,
                                currentPassword = "",
                                repeatNewPassword = "",
                                newPassword = "",
                                enableButton = false
                            )
                        )
                        successMessage("Password has been updated")
                    } else {
                        errorMessage("Something went wrong.")
                    }
                } ?: errorMessage("Something went wrong.")
        }
    }
}