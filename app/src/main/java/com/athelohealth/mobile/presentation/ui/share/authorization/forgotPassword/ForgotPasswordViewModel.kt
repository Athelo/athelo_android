package com.athelohealth.mobile.presentation.ui.share.authorization.forgotPassword

import androidx.lifecycle.SavedStateHandle
import com.athelohealth.mobile.presentation.model.base.InputType
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.member.SendForgotPasswordRequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val sendForgotPasswordRequestUseCase: SendForgotPasswordRequestUseCase,
    savedStateHandle: SavedStateHandle,
) :
    BaseViewModel<ForgotPasswordEvent, ForgotPasswordEffect>() {
    private var username: String =
        ForgotPasswordFragmentArgs.fromSavedStateHandle(savedStateHandle).email
    private var currentViewState =
        ForgotPasswordViewState(
            isLoading = false,
            enableButton = validate(),
            emailError = false,
            initialMessage = username
        )

    private val _viewState = MutableStateFlow(currentViewState)
    val viewState = _viewState.asStateFlow()

    override fun loadData() {}

    override fun handleEvent(event: ForgotPasswordEvent) {
        when (event) {
            ForgotPasswordEvent.ForgotPasswordButtonClick -> selfBlockRun {
                if (validate()) {
                    launchRequest {
                        val result = sendForgotPasswordRequestUseCase(username)
                        withContext(Dispatchers.Main) {
                            successMessage(result.detail)
                        }
                    }
                }
            }
            is ForgotPasswordEvent.InputValueChanged -> when (event.inputType) {
                is InputType.Email -> {
                    username = event.inputType.value
                    currentViewState = currentViewState.copy(enableButton = validate())
                    notifyViewStateChange()
                }
                else -> {}
            }
            ForgotPasswordEvent.BackButtonClick -> notifyEffectChanged(ForgotPasswordEffect.GoBack)
            ForgotPasswordEvent.PPButtonClick -> {}
            ForgotPasswordEvent.ToSButtonClick -> {}
        }
    }

    private fun validate() = username.isNotBlank()

    private fun notifyViewStateChange() {
        launchOnUI {
            _viewState.emit(currentViewState)
        }
    }
}