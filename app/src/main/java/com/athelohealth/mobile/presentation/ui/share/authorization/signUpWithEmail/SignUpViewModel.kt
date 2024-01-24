package com.athelohealth.mobile.presentation.ui.share.authorization.signUpWithEmail

import com.athelohealth.mobile.extensions.debugPrint
import com.athelohealth.mobile.presentation.model.base.InputType
import com.athelohealth.mobile.presentation.model.member.Token
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.SetupPersonalConfigUseCase
import com.athelohealth.mobile.useCase.member.PostUserProfile
import com.athelohealth.mobile.useCase.member.StoreSessionUseCase
import com.athelohealth.mobile.useCase.member.signOut.LogOutUseCase
import com.athelohealth.mobile.utils.AuthorizationException
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val storeSessionUseCase: StoreSessionUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val postUserProfile: PostUserProfile,
    private val setupPersonalInfo: SetupPersonalConfigUseCase
) : BaseViewModel<SignUpEvent, SignUpEffect, SignUpViewState>(SignUpViewState()) {
    private var username = ""
    private var email = ""
    private var password = ""
    private var confirmPassword = ""
    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

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
                currentState = currentState.copy(confirmPasswordError = password != confirmPassword)
            }
            is InputType.Email -> {
                email = input.value
                currentState = currentState.copy(emailError = false)
            }
            is InputType.PersonName -> {
                username = input.value
                currentState = currentState.copy(usernameError = false)
            }
            else -> {/*ignore not used types*/
            }
        }
        currentState = currentState.copy(enableButton = validate())
        notifyStateChange()
    }

    private fun signUser() {
        currentState = currentState.copy(isLoading = true)
        notifyStateChange()
        if (validate()) {
            signUpWithFirebase()
//            launchRequest {
//                val username = this.username
//                val result = signUpUseCase(username, password, confirmPassword)
//                storeSessionUseCase(result.tokenData)
//                storeUserEmailUseCase(username)
//                withContext(Dispatchers.Main) { _effect.emit(SignUpEffect.ShowAdditionalInfoScreen) }
//            }
        } else {
            currentState = currentState.copy(
                isLoading = false,
                emailError = !validateUsername(),
                usernameError = !validateUsername(),
                passwordError = !validatePassword(),
                confirmPasswordError = !validateConfirmPassword()
            )
            debugPrint(currentState)
            notifyStateChange()
        }
    }

    private fun signUpWithFirebase() {
        FirebaseAuth.getInstance().signOut()
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.user?.getIdToken(true)?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            notifyStateChange(currentState.copy(isLoading = true))
                            launchRequest {
                                logOutUseCase()
                                val token = Token(
                                    it.result.token ?: "",
                                    it.result.token ?: "",
                                    it.result.signInProvider ?: "",
                                    "",
                                    it.result.expirationTimestamp.toInt()
                                )
                                storeSessionUseCase(token)
                                postUserProfile(userName = username)
                                // Sign in success, update UI with the signed-in user's information
                                val profile = setupPersonalInfo(token, username)

                                if (profile == null)
                                    throw AuthorizationException("Can't create user profile. Please try to sign in if already have account")
                                else withContext(Dispatchers.Main) { _effect.emit(SignUpEffect.ShowHomeScreen) }
                                pauseLoadingState()
                            }
                        } else errorMessage("Something went wrong.")
                    } ?: errorMessage("Something went wrong.")
                } else errorMessage("Something went wrong.")
            }
    }

    private fun validatePassword() = password.isNotBlank()
    private fun validateConfirmPassword() = confirmPassword.isNotBlank()
    private fun validateUsername() = username.isNotBlank()
    private fun validateEmail() = email.isNotBlank()
    private fun validate(): Boolean {
        return validateUsername() && validateEmail() && validatePassword() && validateConfirmPassword()
    }
}