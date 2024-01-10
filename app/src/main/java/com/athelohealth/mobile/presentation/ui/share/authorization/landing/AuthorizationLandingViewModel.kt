package com.athelohealth.mobile.presentation.ui.share.authorization.landing

import com.athelohealth.mobile.extensions.findGoogleAuthorizationEnum
import com.athelohealth.mobile.presentation.model.enums.Enums
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.SetupPersonalConfigUseCase
import com.athelohealth.mobile.useCase.member.LoadGoogleUserData
import com.athelohealth.mobile.useCase.member.SignWithSocialUseCase
import com.athelohealth.mobile.utils.GoogleOAuthHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import net.openid.appauth.AuthorizationException.GeneralErrors.USER_CANCELED_AUTH_FLOW
import javax.inject.Inject

@HiltViewModel
class AuthorizationLandingViewModel @Inject constructor(
    private val enums: Enums,
    private val loginSocial: SignWithSocialUseCase,
    private val setupPersonalConfigUseCase: SetupPersonalConfigUseCase,
    private val loadGoogleUserData: LoadGoogleUserData,
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
            AuthorizationLandingEvent.SignWithGoogleClick -> selfBlockRun {
                launchOnUI {
                    notifyEffectChanged(AuthorizationLandingEffect.ShowSignWithGoogleScreen)
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
                if (ex?.code == USER_CANCELED_AUTH_FLOW.code) {
                    currentState = currentState.copy(isLoading = false)
                    notifyStateChange()
                    return@launchRequest
                } else if (ex != null) throw ex
                else if (token != null && type != null) {
                    val result = loginSocial(token, type)
                    val username = loadGoogleUserData(token)
                    val profile = setupPersonalConfigUseCase(result.tokenData, username)
                    if (profile == null)
                        notifyEffectChanged(AuthorizationLandingEffect.ShowAdditionalInformationScreen)
                    else notifyEffectChanged(AuthorizationLandingEffect.ShowHomeScreen)
                }
            }
        }
    }

}