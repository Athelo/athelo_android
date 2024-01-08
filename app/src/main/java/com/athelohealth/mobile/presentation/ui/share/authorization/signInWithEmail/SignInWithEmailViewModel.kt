package com.athelohealth.mobile.presentation.ui.share.authorization.signInWithEmail

import com.athelohealth.mobile.presentation.model.base.InputType
import com.athelohealth.mobile.presentation.model.member.Token
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.SetupPersonalConfigUseCase
import com.athelohealth.mobile.useCase.member.PostUserProfile
import com.athelohealth.mobile.utils.app.AppManager
import com.athelohealth.mobile.utils.app.AppType
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignInWithEmailViewModel @Inject constructor(
    private val appManager: AppManager,
    private val setupPersonalInfo: SetupPersonalConfigUseCase,
    private val postUserProfile: PostUserProfile,
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
            currentViewState = currentViewState.copy(
                usernameError = !validateUsername(),
                passwordError = !validatePassword()
            )
        }
    }

   private fun signInWithFirebase(username:String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(username.trim(), password.trim())
            .addOnCompleteListener { task ->
                currentViewState = currentViewState.copy(isLoading = false)
                notifyStateChange()
                if (task.isSuccessful) {
                    val userName = task.result.user?.displayName
                    task.result.user?.getIdToken(true)?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            currentViewState = currentViewState.copy(isLoading = true)
                            notifyStateChange()
                            launchRequest {
                                postUserProfile(userName ?: "")
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
                                currentViewState = currentViewState.copy(isLoading = false)
                                notifyStateChange()
                            }
                        } else { errorMessage("Something went wrong.") }
                    } ?: errorMessage("Something went wrong.")
                } else { errorMessage("Something went wrong.") }
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