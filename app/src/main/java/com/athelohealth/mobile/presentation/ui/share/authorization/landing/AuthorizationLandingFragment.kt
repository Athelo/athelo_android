package com.athelohealth.mobile.presentation.ui.share.authorization.landing

import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthorizationLandingFragment : BaseComposeFragment<AuthorizationLandingViewModel>() {
    override val composeContent: @Composable () -> Unit = {
        AuthorizationLandingScreen(viewModel = viewModel)
    }
    override val viewModel: AuthorizationLandingViewModel by viewModels()

    private val signInHandler =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.handleEvent(
                AuthorizationLandingEvent.SignInWithGoogleResult(requireContext(), it)
            )
        }

    private val signUpHandler =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.handleEvent(
                AuthorizationLandingEvent.SignUpWithGoogleResult(requireContext(), it)
            )
        }

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) {
            when (it) {
                AuthorizationLandingEffect.ShowSignInWithEmailScreen -> routeToSignIn()
                AuthorizationLandingEffect.ShowSignUpWithEmailScreen -> routeToSignUp()
                AuthorizationLandingEffect.ShowSignWithAppleScreen -> showToBeImplementedMessage()
                AuthorizationLandingEffect.ShowSignWithFacebookScreen -> showToBeImplementedMessage()
                AuthorizationLandingEffect.ShowSignInWithGoogleScreen -> signInWithGoogle()
                AuthorizationLandingEffect.ShowSignUpWithGoogleScreen -> signUpWithGoogle()
                AuthorizationLandingEffect.ShowSignWithTwitterScreen -> showToBeImplementedMessage()
                AuthorizationLandingEffect.ShowAdditionalInformationScreen -> routeToAdditionalInformation()
                AuthorizationLandingEffect.ShowHomeScreen -> routeToHome()
                AuthorizationLandingEffect.ShowPrivacyPolicyScreen -> routeToPrivacyPolicyScreen()
                AuthorizationLandingEffect.ShowTermsOfUseScreen -> routeToTermsOfUseScreen()
            }
        }
    }

    private fun signInWithGoogle() {
        GoogleOAuthHelper.signIn(requireContext(), signInHandler)
    }

    private fun signUpWithGoogle() {
        GoogleOAuthHelper.signIn(requireContext(), signUpHandler)
    }
}