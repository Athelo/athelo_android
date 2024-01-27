package com.athelohealth.mobile.presentation.ui.share.authorization.landing

import com.athelohealth.mobile.extensions.findGoogleAuthorizationEnum
import com.athelohealth.mobile.presentation.model.enums.Enums
import com.athelohealth.mobile.presentation.model.member.Token
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.SetupPersonalConfigUseCase
import com.athelohealth.mobile.useCase.member.PostUserProfile
import com.athelohealth.mobile.useCase.member.StoreSessionUseCase
import com.athelohealth.mobile.useCase.member.signOut.LogOutUseCase
import com.athelohealth.mobile.utils.GoogleOAuthHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import net.openid.appauth.AuthorizationException.GeneralErrors.USER_CANCELED_AUTH_FLOW
import javax.inject.Inject

@HiltViewModel
class AuthorizationLandingViewModel @Inject constructor(
    private val storeSessionUseCase: StoreSessionUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val enums: Enums,
    private val setupPersonalInfo: SetupPersonalConfigUseCase,
    private val postUserProfile: PostUserProfile,
) : BaseViewModel<AuthorizationLandingEvent, AuthorizationLandingEffect, AuthorizationLandingViewState>(AuthorizationLandingViewState(false, 0)) {
    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }
    override fun loadData() {}

    override fun handleError(throwable: Throwable) {
        currentState = currentState.copy(isLoading = false)
        notifyStateChange()
        super.handleError(throwable)
    }

    override fun handleEvent(event: AuthorizationLandingEvent) {
        when (event) {
            AuthorizationLandingEvent.SignWithAppleClick -> selfBlockRun {
                notifyEffectChanged(AuthorizationLandingEffect.ShowSignWithAppleScreen)
            }
            AuthorizationLandingEvent.SignInWithEmailClick -> selfBlockRun {
                notifyEffectChanged(AuthorizationLandingEffect.ShowSignInWithEmailScreen)
            }
            AuthorizationLandingEvent.SignUpWithEmailClick -> selfBlockRun {
                notifyEffectChanged(AuthorizationLandingEffect.ShowSignUpWithEmailScreen)
            }
            AuthorizationLandingEvent.SignWithFacebookClick -> selfBlockRun {
                notifyEffectChanged(AuthorizationLandingEffect.ShowSignWithFacebookScreen)
            }
            AuthorizationLandingEvent.SignInWithGoogleClick -> selfBlockRun {
                launchOnUI {
                    notifyEffectChanged(AuthorizationLandingEffect.ShowSignInWithGoogleScreen)
                }
            }
            AuthorizationLandingEvent.SignUpWithGoogleClick -> selfBlockRun {
                launchOnUI {
                    notifyEffectChanged(AuthorizationLandingEffect.ShowSignUpWithGoogleScreen)
                }
            }
            AuthorizationLandingEvent.SignWithTwitterClick -> selfBlockRun {
                notifyEffectChanged(AuthorizationLandingEffect.ShowSignWithTwitterScreen)
            }
            AuthorizationLandingEvent.PPLinkClick -> selfBlockRun {
                notifyEffectChanged(AuthorizationLandingEffect.ShowPrivacyPolicyScreen)
            }
            AuthorizationLandingEvent.TosLinkClick -> selfBlockRun {
                notifyEffectChanged(AuthorizationLandingEffect.ShowTermsOfUseScreen)
            }
            is AuthorizationLandingEvent.TabClick -> {
                notifyStateChange(currentState.copy(selectedTabIndex = event.index))
            }
            is AuthorizationLandingEvent.SignInWithGoogleResult -> {
                currentState = currentState.copy(isLoading = true)
                notifyStateChange()
                handleSignInWithGoogleResponse(event)
            }
            is AuthorizationLandingEvent.SignUpWithGoogleResult -> {
                currentState = currentState.copy(isLoading = true)
                notifyStateChange()
                handleSignUpWithGoogleResponse(event)
            }
        }
    }

    private fun handleSignInWithGoogleResponse(event: AuthorizationLandingEvent.SignInWithGoogleResult) {
        GoogleOAuthHelper.parseResponse(event.context, event.result.data) { response, ex ->
            launchRequest(SupervisorJob() + Dispatchers.IO + requestExceptionHandler) {
                val token = response?.accessToken
                val type = enums.findGoogleAuthorizationEnum()?.id
                if (ex?.code == USER_CANCELED_AUTH_FLOW.code) {
                    currentState = currentState.copy(isLoading = false)
                    notifyStateChange()
                    return@launchRequest
                } else if (ex != null) throw ex
                else if (token != null && type != null) {
                    val authCredential = GoogleAuthProvider.getCredential(response.idToken, response.accessToken)
                    FirebaseAuth.getInstance().signInWithCredential(authCredential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val userName = task.result.user?.displayName
                                task.result.user?.getIdToken(false)?.addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        notifyStateChange(currentState.copy(isLoading = true))
                                        launchRequest {
                                            val token = Token(
                                                it.result.token ?: "",
                                                it.result.token ?: "",
                                                it.result.signInProvider ?: "",
                                                "",
                                                it.result.expirationTimestamp.toInt()
                                            )
                                            storeSessionUseCase(token)
                                            postUserProfile(userName = userName?:"")
                                            // Sign in success, update UI with the signed-in user's information
                                            val profile = setupPersonalInfo(
                                                Token(
                                                    it.result.token ?: "",
                                                    it.result.token ?: "",
                                                    it.result.signInProvider ?: "",
                                                    "",
                                                    it.result.expirationTimestamp.toInt()
                                                ), userName
                                            )


                                            if (profile == null)
                                                withContext(Dispatchers.Main) { _effect.emit(AuthorizationLandingEffect.ShowAdditionalInformationScreen) }
                                            else withContext(Dispatchers.Main) { _effect.emit(AuthorizationLandingEffect.ShowHomeScreen) }
                                            pauseLoadingState()
                                        }
                                    } else errorMessage("Something went wrong.")
                                } ?: errorMessage("Something went wrong.")
                            } else errorMessage("Something went wrong.")
                        }
                }
            }
        }
    }

    private fun handleSignUpWithGoogleResponse(event: AuthorizationLandingEvent.SignUpWithGoogleResult) {
        GoogleOAuthHelper.parseResponse(event.context, event.result.data) { response, ex ->
            launchRequest(SupervisorJob() + Dispatchers.IO + requestExceptionHandler) {
                val token = response?.accessToken
                val type = enums.findGoogleAuthorizationEnum()?.id
                if (ex?.code == USER_CANCELED_AUTH_FLOW.code) {
                    currentState = currentState.copy(isLoading = false)
                    notifyStateChange()
                    return@launchRequest
                } else if (ex != null) throw ex
                else if (token != null && type != null) {
                    val authCredential = GoogleAuthProvider.getCredential(response.idToken, response.accessToken)
                    FirebaseAuth.getInstance().signInWithCredential(authCredential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val userName = task.result.user?.displayName
                                task.result.user?.getIdToken(true)?.addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        notifyStateChange(currentState.copy(isLoading = true))
                                        launchRequest {
                                            logOutUseCase()
                                            val accessToken = Token(
                                                it.result.token ?: "",
                                                it.result.token ?: "",
                                                it.result.signInProvider ?: "",
                                                "",
                                                it.result.expirationTimestamp.toInt()
                                            )
                                            storeSessionUseCase(accessToken)
                                            postUserProfile(userName ?: "")
                                            // Sign in success, update UI with the signed-in user's information
                                            val profile = setupPersonalInfo(accessToken, userName)
                                            if (profile == null)
                                                withContext(Dispatchers.Main) { _effect.emit(AuthorizationLandingEffect.ShowAdditionalInformationScreen) }
                                            else withContext(Dispatchers.Main) { _effect.emit(AuthorizationLandingEffect.ShowHomeScreen) }
                                            pauseLoadingState()
                                        }
                                    } else errorMessage("Something went wrong.")
                                } ?: errorMessage("Something went wrong.")
                            } else errorMessage("Something went wrong.")
                        }
                }
            }
        }
    }
}