package com.athelohealth.mobile.presentation.ui.share.authorization.forgotPassword

import androidx.lifecycle.SavedStateHandle
import com.athelohealth.mobile.presentation.model.base.InputType
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ForgotPasswordEvent, ForgotPasswordEffect, ForgotPasswordViewState>(ForgotPasswordViewState(
    isLoading = false,
    enableButton = false,
    emailError = false,
    initialMessage = ""
)) {
    private var username: String =
        ForgotPasswordFragmentArgs.fromSavedStateHandle(savedStateHandle).email

    init {
        notifyStateChange(
            ForgotPasswordViewState(
                isLoading = false,
                enableButton = validate(),
                emailError = false,
                initialMessage = username
            )
        )
    }

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    override fun loadData() {}

    override fun handleEvent(event: ForgotPasswordEvent) {
        when (event) {
            ForgotPasswordEvent.ForgotPasswordButtonClick -> selfBlockRun {
                if (validate()) {
                    launchRequest {
                        FirebaseAuth.getInstance().sendPasswordResetEmail(username).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                    successMessage("Email sent.")
                            }
                        }
                    }
//                    launchRequest {
//                        val result = sendForgotPasswordRequestUseCase(username)
//                        withContext(Dispatchers.Main) {
//                            successMessage(result.detail)
//                        }
//                    }
                }
            }
            is ForgotPasswordEvent.InputValueChanged -> when (event.inputType) {
                is InputType.Email -> {
                    username = event.inputType.value
                    notifyStateChange(currentState.copy(enableButton = validate()))
                }
                else -> {}
            }
            ForgotPasswordEvent.BackButtonClick -> notifyEffectChanged(ForgotPasswordEffect.GoBack)
            ForgotPasswordEvent.PPButtonClick -> {}
            ForgotPasswordEvent.ToSButtonClick -> {}
        }
    }

    private fun validate() = username.isNotBlank()


}