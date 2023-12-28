package com.athelohealth.mobile.presentation.ui.share.authorization.signInWithEmail

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInWithEmailFragment : BaseComposeFragment<SignInWithEmailViewModel>() {
    override val composeContent: @Composable () -> Unit = {
        SignInScreen(viewModel = viewModel)
    }
    override val viewModel: SignInWithEmailViewModel by viewModels()

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) {
            when (it) {
                SignInWithEmailEffect.GoBack -> routeToBackScreen()
                SignInWithEmailEffect.ShowAdditionalInformationScreen -> routeToAdditionalInformation()
                is SignInWithEmailEffect.ShowForgotPassword -> routeToForgotPassword(it.email)
                SignInWithEmailEffect.ShowHomeScreen -> routeToHome()
                SignInWithEmailEffect.ShowPrivacyPolicyScreen -> routeToPrivacyPolicyScreen()
                SignInWithEmailEffect.ShowTermsOfUseScreen -> routeToTermsOfUseScreen()
                is SignInWithEmailEffect.ShowRoleScreen -> routeToSelectRole(initialFlow = it.initFlow)
            }
        }
    }
}