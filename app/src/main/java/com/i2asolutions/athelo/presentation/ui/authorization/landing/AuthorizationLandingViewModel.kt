package com.i2asolutions.athelo.presentation.ui.authorization.landing

import com.i2asolutions.athelo.extensions.findGoogleAuthorizationEnum
import com.i2asolutions.athelo.presentation.model.enums.Enums
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.SetupPersonalConfigUseCase
import com.i2asolutions.athelo.useCase.member.LoadGoogleUserData
import com.i2asolutions.athelo.useCase.member.SignWithSocialUseCase
import com.i2asolutions.athelo.utils.GoogleOAuthHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthorizationLandingViewModel @Inject constructor(
    private val enums: Enums,
    private val loginSocial: SignWithSocialUseCase,
    private val setupPersonalConfigUseCase: SetupPersonalConfigUseCase,
    private val loadGoogleUserData: LoadGoogleUserData,
) : BaseViewModel<AuthorizationLandingEvent, AuthorizationLandingEffect>() {
    private var currentState = AuthorizationLandingViewState(false, 0)
    private val _state = MutableStateFlow(currentState)
    val state = _state.asStateFlow()
    override fun loadData() {}

    override fun handleError(throwable: Throwable) {
        currentState = currentState.copy(isLoading = false)
        notifyStateChange()
        super.handleError(throwable)
    }

    override fun handleEvent(event: AuthorizationLandingEvent) {
        when (event) {
            AuthorizationLandingEvent.SignWithAppleClick -> selfBlockRun {
                notifyEffect(
                    AuthorizationLandingEffect.ShowSignWithAppleScreen
                )
            }
            AuthorizationLandingEvent.SignInWithEmailClick -> selfBlockRun {
                notifyEffect(
                    AuthorizationLandingEffect.ShowSignInWithEmailScreen
                )
            }
            AuthorizationLandingEvent.SignUpWithEmailClick -> selfBlockRun {
                notifyEffect(
                    AuthorizationLandingEffect.ShowSignUpWithEmailScreen
                )
            }
            AuthorizationLandingEvent.SignWithFacebookClick -> selfBlockRun {
                notifyEffect(
                    AuthorizationLandingEffect.ShowSignWithFacebookScreen
                )
            }
            AuthorizationLandingEvent.SignWithGoogleClick -> selfBlockRun {
                launchOnUI {
                    notifyEffect(
                        AuthorizationLandingEffect.ShowSignWithGoogleScreen
                    )
                }
            }
            AuthorizationLandingEvent.SignWithTwitterClick -> selfBlockRun {
                notifyEffect(
                    AuthorizationLandingEffect.ShowSignWithTwitterScreen
                )
            }
            AuthorizationLandingEvent.PPLinkClick -> selfBlockRun {
                notifyEffect(AuthorizationLandingEffect.ShowPrivacyPolicyScreen)
            }
            AuthorizationLandingEvent.TosLinkClick -> selfBlockRun {
                notifyEffect(AuthorizationLandingEffect.ShowTermsOfUseScreen)
            }
            is AuthorizationLandingEvent.TabClick -> {
                updateTabIndexAndNotify(event.index)
            }
            is AuthorizationLandingEvent.SignWithGoogleResult -> {
                currentState = currentState.copy(isLoading = true)
                notifyStateChange()
                handleSignWithGoogleResponse(event)
            }
        }
    }

    private fun handleSignWithGoogleResponse(event: AuthorizationLandingEvent.SignWithGoogleResult) {
        GoogleOAuthHelper.parseResponse(event.context, event.result.data) { response, ex ->
            launchRequest(SupervisorJob() + Dispatchers.IO + requestExceptionHandler) {
                val token = response?.accessToken
                val type = enums.findGoogleAuthorizationEnum()?.id
                if (ex != null) throw ex
                else if (token != null && type != null) {
                    val result = loginSocial(token, type)
                    val username = loadGoogleUserData(token)
                    val profile = setupPersonalConfigUseCase(result.tokenData, username)
                    if (profile == null)
                        notifyEffect(AuthorizationLandingEffect.ShowAdditionalInformationScreen)
                    else notifyEffect(AuthorizationLandingEffect.ShowHomeScreen)
                }
            }
        }
    }

    private fun updateTabIndexAndNotify(index: Int) {
        currentState = currentState.copy(selectedTabIndex = index)
        launchOnUI { _state.emit(currentState) }
    }

    private fun notifyStateChange() {
        launchOnUI {
            _state.emit(currentState)
        }
    }

    private fun notifyEffect(effect: AuthorizationLandingEffect) {
        launchOnUI { _effect.emit(effect) }
    }
}