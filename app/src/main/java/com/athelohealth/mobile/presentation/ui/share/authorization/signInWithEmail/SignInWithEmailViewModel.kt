package com.athelohealth.mobile.presentation.ui.share.authorization.signInWithEmail

import com.athelohealth.mobile.presentation.model.base.InputType
import com.athelohealth.mobile.presentation.model.member.Token
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.SetupPersonalConfigUseCase
import com.athelohealth.mobile.utils.app.AppManager
import com.athelohealth.mobile.utils.app.AppType
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignInWithEmailViewModel @Inject constructor(
    private val appManager: AppManager,
    private val setupPersonalInfo: SetupPersonalConfigUseCase
) :
    BaseViewModel<SignInWithEmailEvent, SignInWithEmailEffect, SignInWithEmailViewState>(SignInWithEmailViewState(enableButton = false, usernameError = false, passwordError = false)) {
    private var username: String = ""
    private var password: String = ""

    override fun loadData() {}

    override fun pauseLoadingState() {
        notifyStateChange(currentState.copy(isLoading = false))
    }

    override fun handleEvent(event: SignInWithEmailEvent) {
        when (event) {
            SignInWithEmailEvent.BackButtonClick -> notifyEffectChanged(SignInWithEmailEffect.GoBack)
            SignInWithEmailEvent.ForgotPasswordClick -> selfBlockRun {
                currentState = currentState.copy(username = username)
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
            notifyStateChange(currentState.copy(isLoading = true))
            signInWithFirebase(username, password)
//            launchRequest(SupervisorJob() + Dispatchers.IO + requestExceptionHandler) {
//                val result = signInUseCase(username, password)
//                val profile = setupPersonalInfo(result.tokenData, username)
//                if (profile == null)
//                    withContext(Dispatchers.Main) { _effect.emit(SignInWithEmailEffect.ShowAdditionalInformationScreen) }
//                else if(appManager.appType.value == AppType.Unknown) withContext(Dispatchers.Main){ _effect.emit(SignInWithEmailEffect.ShowRoleScreen(true)) }
//                else withContext(Dispatchers.Main) { _effect.emit(SignInWithEmailEffect.ShowHomeScreen) }
//            }
        } else {
            currentState = currentState.copy(
                usernameError = !validateUsername(),
                passwordError = !validatePassword()
            )
        }
    }

   private fun signInWithFirebase(username:String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(username.trim(), password.trim())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.user?.getIdToken(true)?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            notifyStateChange(currentState.copy(isLoading = true))
                            launchRequest {
                                // Sign in success, update UI with the signed-in user's information
                                val profile = setupPersonalInfo(
                                    Token(
                                        it.result.token ?: "",
                                        it.result.token ?: "",
                                        it.result.signInProvider ?: "",
                                        "",
                                        it.result.expirationTimestamp.toInt()
                                    ), username
                                )

                                if (profile == null)
                                    withContext(Dispatchers.Main) { _effect.emit(SignInWithEmailEffect.ShowAdditionalInformationScreen) }
                                else if (appManager.appType.value == AppType.Unknown)
                                    withContext( Dispatchers.Main) { _effect.emit(SignInWithEmailEffect.ShowRoleScreen(true)) }
                                else withContext(Dispatchers.Main) { _effect.emit(SignInWithEmailEffect.ShowHomeScreen) }
                                pauseLoadingState()
                            }
                        } else errorMessage("Something went wrong.")
                    } ?: errorMessage("Something went wrong.")
                } else errorMessage("Something went wrong.")
            }
    }

    private fun handleInputChange(inputType: InputType) {
        when (inputType) {
            is InputType.Password -> {
                password = inputType.value
                currentState = currentState.copy(passwordError = false)
            }
            is InputType.Email -> {
                username = inputType.value
                currentState = currentState.copy(usernameError = false)
            }
            else -> {}
        }
        notifyStateChange(currentState.copy(enableButton = validate()))
    }

    private fun validatePassword() = password.isNotBlank()
    private fun validateUsername() = username.isNotBlank()
    private fun validate(): Boolean {
        return validateUsername() && validatePassword()
    }
}