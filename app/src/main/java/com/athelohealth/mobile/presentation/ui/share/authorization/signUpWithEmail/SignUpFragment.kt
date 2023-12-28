package com.athelohealth.mobile.presentation.ui.share.authorization.signUpWithEmail

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToAdditionalInformation
import com.athelohealth.mobile.utils.routeToBackScreen
import com.athelohealth.mobile.utils.routeToPrivacyPolicyScreen
import com.athelohealth.mobile.utils.routeToTermsOfUseScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseComposeFragment<SignUpViewModel>() {
    override val composeContent: @Composable () -> Unit = {
        SignUpScreen(viewModel = viewModel)
    }
    override val viewModel: SignUpViewModel by viewModels()

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) {
            when (it) {
                SignUpEffect.GoBack -> routeToBackScreen()
                SignUpEffect.ShowAdditionalInfoScreen -> routeToAdditionalInformation()
                SignUpEffect.ShowPPScreen -> routeToPrivacyPolicyScreen()
                SignUpEffect.ShowToSScreen -> routeToTermsOfUseScreen()
            }
        }
    }
}